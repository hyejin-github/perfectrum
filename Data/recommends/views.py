from operator import le
import re
import pandas as pd
import numpy as np
from sklearn.decomposition import randomized_svd, non_negative_factorization
from surprise import Dataset, NMF
from surprise.model_selection import cross_validate
from django.core.cache import cache
from django.shortcuts import render
from rest_framework.response import Response
from rest_framework import status
from rest_framework.decorators import api_view
from .models import Perfumes, Reviews, HaveLists, WishLists,Users,UserDetailLogs, UserSearchLogs, Accords, AccordsClasses
from .serializers import PerfumeSerializer, ReviewSerializer, PerfumeListSerializer
import pymysql
import json
# Create your views here.
def compute_cos_similarity(v1, v2) :
    norm1 = np.sqrt(np.sum(np.square(v1)))
    norm2 = np.sqrt(np.sum(np.square(v2)))
    dot = np.dot(v1,v2)
    return dot / (norm1 * norm2)
    
@api_view(['GET'])
def collaboration(request):
    data = request.query_params
    target_user_idx = changeInt(data.get("user_idx"))

    users = Users.objects.values_list('idx').order_by('-idx')
    df = pd.DataFrame(list(Reviews.objects.all().values('user_idx','perfume_idx','total_score')))
    df2 = pd.DataFrame(list(HaveLists.objects.all().values('user_idx','perfume_idx','is_delete')))
    Review_score = list(zip(df['user_idx'],df['perfume_idx'],df['total_score']))
    Have_score = list(zip(df2['user_idx'],df2['perfume_idx'],df2['is_delete']))
    raw_data = np.array(Review_score, dtype=int)
    raw_data2 = np.array(Have_score, dtype=int)
    # print(df)
    # n_users = np.max(raw_data[:,0]) # 유저의 최댓값
    n_users = users[0][0]
    n_perfumes = 765
    shape = (n_users+1, n_perfumes+1)
    adj_matrix = np.zeros(shape=shape)
    for user_id, perfume_id, rating in raw_data :
        adj_matrix[user_id][perfume_id] = rating
    for user_id, perfume_id, is_delete in raw_data2 :
        if is_delete :
            continue
        adj_matrix[user_id][perfume_id] += 2.5 
    U,S,V = randomized_svd(adj_matrix, n_components=2)
    # print(U)
    # print(V)
    S = np.diag(S)
    np.matmul(np.matmul(U,S), V)
    my_id, my_vector = target_user_idx, U[target_user_idx]
    best_match, best_match_id, best_match_vector = -1, -1, []
    for user_id, user_vector in enumerate(U) :
        if my_id != user_id :
            cos_similarity = compute_cos_similarity(my_vector, user_vector)
            if cos_similarity > best_match :
                best_match = cos_similarity
                best_match_id = user_id
                best_match_vector = user_vector
    # print(f'Best Match : {best_match}, Best Match ID : {best_match_id}]')
    recommend_list = []
    for i, log in enumerate(zip(adj_matrix[my_id], adj_matrix[best_match_id])) :
        log1, log2 = log
        if log1 < 1. and log2 > 0. :
            recommend_list.append(i)
    perfume = list(Perfumes.objects.filter(idx__in=recommend_list))
    serializer = PerfumeListSerializer(perfume, many=True)
    return Response(serializer.data, status=status.HTTP_200_OK)



def changeInt(variable):
    try:
        return int(variable)
    except:
        return False


@api_view(['GET'])
def collaboration2(request) :
    
    data = request.query_params
    target_perfume_idx = changeInt(data.get("perfume_idx"))
    
    # 선택된 향수 정보 가져오기 와서 향수 정보가 없는 경우 400
    target_perfume = Perfumes.objects.filter(idx = target_perfume_idx)
    
    if not target_perfume:
        return Response(status=status.HTTP_400_BAD_REQUEST)
    
    #레디스에서 추천된 정보 받아오기 없다면 추천 알고리즘 진행
    target_recommends = cache.get(target_perfume_idx)
    if target_recommends is not None:
        return Response(target_recommends, status=status.HTTP_200_OK)       

    # 선호 리스트, 보유 리스트 받아오기
    conn = pymysql.connect(host='j7c105.p.ssafy.io', user='ssafy', password='ssafy', db='S07P22C105', charset='utf8')

    cur = conn.cursor()

    sql = 'select * from users order by idx desc limit 1'
    cur.execute(sql)
    data = cur.fetchall()
    n_users = data[0][5]

    sql = 'select * from perfumes order by idx desc limit 1'
    cur.execute(sql)
    data = cur.fetchall()
    n_perfumes = data[0][0]

    shape = (n_perfumes + 1, n_users + 1)
    matrix = np.zeros(shape, dtype=int)

    sql = 'select * from have_lists'
    cur.execute(sql)
    data = cur.fetchall()
    raw_data = np.array(data, dtype=int)
    for idx, user_idx, is_delete, perfume_idx in raw_data:
        if is_delete:
            continue
        matrix[perfume_idx][user_idx] += 1


    sql = 'select * from wish_lists'
    cur.execute(sql)
    data = cur.fetchall()
    raw_data = np.array(data, dtype=int)
    for idx, user_idx, is_delete, perfume_idx in raw_data:
        if is_delete:
            continue
        matrix[perfume_idx][user_idx] += 1


    sql = 'select * from user_detail_logs'
    cur.execute(sql)
    data = cur.fetchall()
    raw_data = np.array(data, dtype=int)
    print(raw_data)
    for idx, user_idx, perfume_idx in raw_data:
        matrix[perfume_idx][user_idx] += 1
    conn.close()

    target_perfume_vector = matrix[target_perfume_idx]
    best_match, best_match_id, best_match_vector = -1, -1, []
    for perfume_id, perfume_vector in enumerate(matrix):
        if target_perfume_idx != perfume_id:
            similarity = np.dot(target_perfume_vector, perfume_vector)
            if similarity > best_match:
                best_match = similarity
                best_match_id = perfume_id
                best_match_vector = perfume_vector
    perfume = []
    for i in range(len(best_match_vector)):
        if best_match_vector[i]:
            for item in HaveLists.objects.filter(user_idx=i):
                perfume.append(item.perfume_idx)
            for item in WishLists.objects.filter(user_idx=i):
                perfume.append(item.perfume_idx)
    serializer = PerfumeListSerializer(perfume[:8], many=True)
    
    # 레디스에 결과 저장하기
    cache.set(target_perfume_idx, serializer.data)
    return Response(serializer.data, status=status.HTTP_200_OK)

@api_view(['GET'])
def collaboration3(request) :
    data = request.query_params
    target_user_idx = changeInt(data.get("user_idx"))

    users = Users.objects.values_list('idx').order_by('-idx')
    df = pd.DataFrame(list(WishLists.objects.all().values('user_idx','perfume_idx','is_delete')))
    df2 = pd.DataFrame(list(UserDetailLogs.objects.all().values('user_idx','perfume_idx')))
    df3 = pd.DataFrame(list(UserSearchLogs.objects.all().values('user_idx','gender','duration')))
    perfumes = list(Perfumes.objects.values_list('idx','gender','longevity'))

    wish_score = list(zip(df['user_idx'],df['perfume_idx'],df['is_delete']))
    detail_score = list(zip(df2['user_idx'],df2['perfume_idx']))
    search_info = list(zip(df3['user_idx'],df3['gender'],df3['duration']))
    search_score = []
    for i in search_info :
        for j in perfumes :
            if i[1] == j[1] and i[2] == j[2] :
                search_score.append((i[0],j[0]))
    wish_data = np.array(wish_score, dtype=int)
    detail_data = np.array(detail_score, dtype=int)
    search_data = np.array(search_score, dtype=int)
    n_user = users[0][0]
    n_perfumes = 765
    shape = (n_user+1, n_perfumes+1)
    adj_matrix = np.zeros(shape=shape)
    recommend_matrix = np.zeros(shape=shape)

    for user_id, perfume_id, is_delete in wish_data :
        if is_delete :
            continue
        adj_matrix[user_id][perfume_id] += 2
        recommend_matrix[user_id][perfume_id] += 1
    for user_id, perfume_id in detail_data :
        adj_matrix[user_id][perfume_id] += 0.5
        # recommend_matrix[user_id][perfume_id] += 1
    for user_id, perfume_id in search_data :
        adj_matrix[user_id][perfume_id] += 0.1
    A, B ,iter = non_negative_factorization(adj_matrix, n_components=2)
    my_id, my_vector = target_user_idx, A[target_user_idx]
    best_match, best_match_id, best_match_vector = -1, -1, []
    for user_id, user_vector in enumerate(A) :
        if my_id != user_id :
            cos_similarity = compute_cos_similarity(my_vector, user_vector)
            if cos_similarity > best_match :
                best_match = cos_similarity
                best_match_id = user_id
                best_match_vector = user_vector
    recommend_list = []
    for i, log in enumerate(zip(recommend_matrix[my_id], recommend_matrix[best_match_id])) :
        log1, log2 = log
        if log1 < 1. and log2 > 0. :
            recommend_list.append(i)
    perfume = list(Perfumes.objects.filter(idx__in=recommend_list))
    serializer = PerfumeListSerializer(perfume, many=True)
    return Response(serializer.data, status=status.HTTP_200_OK)