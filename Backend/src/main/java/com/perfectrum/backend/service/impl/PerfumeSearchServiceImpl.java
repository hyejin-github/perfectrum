package com.perfectrum.backend.service.impl;

import com.perfectrum.backend.domain.entity.*;
import com.perfectrum.backend.domain.repository.*;
import com.perfectrum.backend.dto.Search.PerfumeSearchDto;
import com.perfectrum.backend.dto.perfume.PerfumeViewDto;
import com.perfectrum.backend.mapper.PerfumeViewMapper;
import com.perfectrum.backend.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PerfumeSearchServiceImpl implements SearchService {

    private PerfumeRepository perfumeRepository;
    private UserRepository userRepository;
    private AccordClassRepository accordClassRepository;

    private HaveListRepository haveListRepository;
    private WishListRepository wishListRepository;
    private UserSearchLogRepository userSearchLogRepository;

    private PerfumeViewMapper perfumeViewMapper;

    @Autowired
    PerfumeSearchServiceImpl(PerfumeRepository perfumeRepository,UserRepository userRepository,AccordClassRepository accordClassRepository,
                             HaveListRepository haveListRepository,WishListRepository wishListRepository, UserSearchLogRepository userSearchLogRepository,
                             PerfumeViewMapper perfumeViewMapper){
        this.perfumeRepository = perfumeRepository;
        this.userRepository = userRepository;
        this.accordClassRepository = accordClassRepository;
        this.haveListRepository = haveListRepository;
        this.wishListRepository = wishListRepository;
        this.userSearchLogRepository = userSearchLogRepository;
        this.perfumeViewMapper = perfumeViewMapper;
    }


    @Override
    public Map<String, Object> searchPerfume(String decodeId, PerfumeSearchDto perfumeSearchDto) {
        Map<String,Object> data = new HashMap<>();

        List<String> genderList_kr = perfumeSearchDto.getGender();
        List<String> genderList = new ArrayList<>();
        if(genderList_kr.size()==0){
            genderList.add("Men");
            genderList.add("Women");
            genderList.add("Unisex");
        }else {
            for (String gender : genderList_kr) {
                if (gender.equals("남자")) {
                    genderList.add("Men");
                } else if (gender.equals("여자")) {
                    genderList.add("Women");
                } else {
                    genderList.add("Unisex");
                }
            }
        }
        System.out.println(genderList_kr.size());
        List<String> durationList_kr = perfumeSearchDto.getDuration();
        List<Integer> durationList = new ArrayList<>();
        if(durationList_kr.size()==0) {
            for (int i = 1; i <= 5; i++) {
                durationList.add(i);
            }
        }else{
            for (String duration : durationList_kr) {
                switch (duration) {
                    case "매우 약함":
                        durationList.add(1);
                        break;
                    case "약함":
                        durationList.add(2);
                        break;
                    case "적당함":
                        durationList.add(3);
                        break;
                    case "강함":
                        durationList.add(4);
                        break;
                    case "매우 강함":
                        durationList.add(5);
                        break;

                }
            }
        }
        List<AccordClassEntity> accordClassList = new ArrayList<>();
        List<String> accords = perfumeSearchDto.getAccordClass();

        System.out.println("어코드 개수 : " + accordClassList.size());
        if(accords.size()==0){
            for(int i=1;i<=8;i++){
                accordClassList.add(accordClassRepository.findByIdx(i));
            }
        }else{
            for(String accord : accords){
                switch(accord){
                    case "꽃 향기":
                        accordClassList.add(accordClassRepository.findByIdx(2));
                        break;
                    case "풀 향기":
                        accordClassList.add(accordClassRepository.findByIdx(3));
                        break;
                    case "과일 향":
                        accordClassList.add(accordClassRepository.findByIdx(4));
                        break;
                    case "달콤한 향":
                        accordClassList.add(accordClassRepository.findByIdx(8));
                        break;
                    case "매운 향":
                        accordClassList.add(accordClassRepository.findByIdx(5));
                        break;
                    case "톡쏘는 향":
                        accordClassList.add(accordClassRepository.findByIdx(1));
                        break;
                    case "야성적인 향":
                        accordClassList.add(accordClassRepository.findByIdx(6));
                        break;
                    case "인공적인 향":
                        accordClassList.add(accordClassRepository.findByIdx(7));
                        break;
                }
            }
        }


        Integer lastIdx = perfumeSearchDto.getLastIdx();
        Integer pageSize = perfumeSearchDto.getPageSize();
        Pageable pageable = Pageable.ofSize(pageSize);


        if(lastIdx == null){
            lastIdx = perfumeRepository.findTop1ByOrderByIdxDesc().getIdx() + 1;
        }

        Slice<PerfumeEntity> searchList = perfumeRepository.findAllByGenderAndLongevityAndAccordClass(genderList, durationList, accordClassList, lastIdx, pageable);
        List<PerfumeEntity> list = perfumeRepository.findAllByGenderAndLongevityAndAccordClass2(genderList, durationList, accordClassList);
        System.out.println("향수 갯수 : " + list.size());
        List<PerfumeViewDto> resultList = new ArrayList<>();
        if(!searchList.isEmpty()){
            boolean hasNext = searchList.hasNext();
            data.put("hasNext",hasNext);
            for(PerfumeEntity pe : searchList){
                PerfumeViewDto perfumeViewDto = PerfumeViewDto.builder()
                        .idx(pe.getIdx())
                        .brandName(pe.getBrandName())
                        .perfumeName(pe.getPerfumeName())
                        .concentration(pe.getConcentration())
                        .gender(pe.getGender())
                        .scent(pe.getScent())
                        .topNotes(pe.getTopNotes())
                        .middleNotes(pe.getMiddleNotes())
                        .baseNotes(pe.getBaseNotes())
                        .itemRating(pe.getItemRating())
                        .perfumeImg(pe.getPerfumeImg())
                        .description(pe.getDescription())
                        .seasons(pe.getSeasons())
                        .timezone(pe.getTimezone())
                        .longevity(pe.getLongevity())
                        .sillage(pe.getSillage())
                        .wishCount(Long.valueOf(Optional.ofNullable(haveListRepository.countByPerfumeIdxAndIsDelete(pe.getIdx(),false)).orElse(0L)).intValue())
                        .haveCount(Long.valueOf(Optional.ofNullable(wishListRepository.countByPerfumeIdxAndIsDelete(pe.getIdx(),false)).orElse(0L)).intValue())
                        .build();

                resultList.add(perfumeViewDto);
            }
            data.put("perfumeList",resultList);

        }else{
            data.put("perfumeList", null);
            // 결과 없을 때 베스트 향수 6개만 추천
            Optional<UserEntity> userEntityOptional = userRepository.findByUserId(decodeId);
            List<PerfumeViewDto> bestPerfume = new ArrayList<>();

            if(userEntityOptional.isPresent()){
                UserEntity user = userEntityOptional.get();
                String gender = user.getGender();
                String season = user.getSeasons();
                Integer accordClass = user.getAccordClass();

                if(gender != null){
                    AccordClassEntity accordClassEntity =  accordClassRepository.findByIdx(accordClass);

                    Pageable top6 = Pageable.ofSize(6);
                    List<PerfumeEntity> perfumes = perfumeRepository.findBest6Perfumes(gender,season,accordClassEntity,top6);

                    for(PerfumeEntity p : perfumes){
                        PerfumeViewDto perfumeViewDto = perfumeViewMapper.toDto(p);
                        bestPerfume.add(perfumeViewDto);
                    }
                }else{
                    List<PerfumeEntity> perfumes = perfumeRepository.findTop6ByOrderByItemRatingDesc();

                    for(PerfumeEntity p : perfumes){
                        PerfumeViewDto perfumeViewDto = perfumeViewMapper.toDto(p);
                        bestPerfume.add(perfumeViewDto);
                    }
                }
            }else{
                List<PerfumeEntity> perfumes = perfumeRepository.findTop6ByOrderByItemRatingDesc();
                for(PerfumeEntity p : perfumes){
                    PerfumeViewDto perfumeViewDto = perfumeViewMapper.toDto(p);
                    bestPerfume.add(perfumeViewDto);
                }
            }
            data.put("bestPerfumeList", bestPerfume);
        }

        Optional<UserEntity> userEntityOptional = userRepository.findByUserId(decodeId);
        if(userEntityOptional.isPresent()){
            UserEntity user = userEntityOptional.get();
            for(int i=0; i<genderList.size(); i++){
                for(int j=0; j<durationList.size(); j++){
                    for(int k=0; k<accordClassList.size(); k++){

                        UserSearchLogEntity userSearchLogEntity = UserSearchLogEntity.builder()
                                .user(user)
                                .gender(genderList.get(i))
                                .duration(durationList.get(j))
                                .accordClass(accordClassList.get(k))
                                .build();

                        userSearchLogRepository.save(userSearchLogEntity);
                    }
                }
            }
        }
        return data;
    }
}