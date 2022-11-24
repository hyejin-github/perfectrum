package com.perfectrum.backend.service;

import com.perfectrum.backend.dto.review.MyReviewDto;
import com.perfectrum.backend.dto.review.MyReviewListDto;
import com.perfectrum.backend.dto.user.UserInfoDto;
import com.perfectrum.backend.dto.user.UserMoreInfoDto;
import com.perfectrum.backend.dto.user.UserUpdateInfoDto;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserInfoDto getUserInfo(String decodeId);

    void addMoreUserInfo(String decodeId, UserMoreInfoDto userMoreInfoDto);

    UserInfoDto updateUserInfo(String decodeId, UserUpdateInfoDto userUpdateInfoDto);

    boolean checkNickName(String nickname);

    void deleteUser(String decodeId);

    Map<String, Object> viewMyReviews(String decodeId, MyReviewListDto myReviewListDto);

    Object getTotalReviews(String decodeId);

    Object getAvgReviews(String decodeId);

    String getUserId(String nickname);
}