package com.perfectrum.backend.service.impl;

import com.perfectrum.backend.domain.entity.*;
import com.perfectrum.backend.domain.repository.*;
import com.perfectrum.backend.dto.perfume.PerfumeViewDto;
import com.perfectrum.backend.dto.survey.SurveyDto;
import com.perfectrum.backend.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SurveyServiceImpl implements SurveyService {

    private PerfumeRepository perfumeRepository;
    private UserRepository userRepository;

    private HaveListRepository haveListRepository;
    private WishListRepository wishListRepository;
    private AccordClassRepository accordClassRepository;
    private AccordRepository accordRepository;
    private SurveyRepository surveyRepository;
    @Autowired
    SurveyServiceImpl(PerfumeRepository perfumeRepository,UserRepository userRepository,WishListRepository wishListRepository,
                      HaveListRepository haveListRepository, AccordClassRepository accordClassRepository, AccordRepository accordRepository,
                      SurveyRepository surveyRepository){
        this.perfumeRepository = perfumeRepository;
        this.userRepository = userRepository;
        this.wishListRepository = wishListRepository;
        this.haveListRepository = haveListRepository;
        this.accordClassRepository = accordClassRepository;
        this.accordRepository = accordRepository;
        this.surveyRepository = surveyRepository;
    }
    @Override
    public Map<String, Object> surveyResult(String dedocdId, SurveyDto surveyDto) {
        Map<String,Object> data = new HashMap<>();
        Random random = new Random();

        String gender = surveyDto.getGender();
        if(gender.equals("남자")){
            gender = "Men";
        }else if(gender.equals("여자")){
            gender = "Women";
        }else{
            gender = null;
        }
        String season = surveyDto.getSeason();
        switch (season){
            case "봄":
                season = "spring";
                break;
            case "여름":
                season = "summer";
                break;
            case "가을":
                season = "fall";
                break;
            case "겨울":
                season = "winter";
                break;
        }
        String accordClass_s = surveyDto.getAccordClass();
        Integer accordClass = 0;

        switch(accordClass_s){
            case "꽃 향기":
                accordClass = 2;
                break;
            case "풀 향기":
                accordClass = 3;
                break;
            case "과일 향":
                accordClass = 4;
                break;
            case "달콤한 향":
                accordClass = 8;
                break;
            case "매운 향":
                accordClass = 5;
                break;
            case "톡쏘는 향":
                accordClass = 1;
                break;
            case "야성적인 향":
                accordClass = 6;
                break;
            case "인공적인 향":
                accordClass = 7;
                break;
        }
        AccordClassEntity accordClassEntity = accordClassRepository.findByIdx(accordClass);

        List<PerfumeEntity> surveyList = new ArrayList<>();
        Integer duration = surveyDto.getLongevity();
        if(duration==0){
            if(gender==null){
                surveyList = perfumeRepository.findBySeasonAndWeakLongevityAndAccordClass(season, accordClassEntity);
            }else{
                surveyList = perfumeRepository.findByGenderAndSeasonAndWeakLongevityAndAccordClass(gender, season, accordClassEntity);
            }
        }else{
            if(gender==null){
                surveyList = perfumeRepository.findBySeasonAndStrongLongevityAndAccordClass(season, accordClassEntity);
            }else{
                surveyList = perfumeRepository.findByGenderAndSeasonAndStrongLongevityAndAccordClass(gender, season, accordClassEntity);
            }
        }

        List<PerfumeEntity> resultList = new ArrayList<>();
        Integer max = 0,cnt = 0;
        for(int i=0;i<surveyList.size();i++){

            cnt = accordRepository.countByAccordClass(surveyList.get(i), accordClassEntity);

            if(max < cnt){
                max = cnt;
                resultList.clear();
                resultList.add(surveyList.get(i));
            }else if(max == cnt){
                resultList.add(surveyList.get(i));
            }
        }

        PerfumeEntity perfume;
        if(resultList.size() != 1){
            perfume = resultList.get(random.nextInt(resultList.size()));
        }else {
            perfume = resultList.get(0);
        }
        Integer haveCount = Long.valueOf(Optional.ofNullable(haveListRepository.countByPerfumeIdxAndIsDelete(perfume.getIdx(),false)).orElse(0L)).intValue();

        Integer wishCount = Long.valueOf(Optional.ofNullable(wishListRepository.countByPerfumeIdxAndIsDelete(perfume.getIdx(), false)).orElse(0L)).intValue();
        PerfumeViewDto perfumeViewDto = PerfumeViewDto.builder()
                .idx(perfume.getIdx())
                .brandName(perfume.getBrandName())
                .perfumeName(perfume.getPerfumeName())
                .concentration(perfume.getConcentration())
                .gender(perfume.getGender())
                .scent(perfume.getScent())
                .topNotes(perfume.getTopNotes())
                .middleNotes(perfume.getMiddleNotes())
                .baseNotes(perfume.getBaseNotes())
                .itemRating(perfume.getItemRating())
                .perfumeImg(perfume.getPerfumeImg())
                .description(perfume.getDescription())
                .seasons(perfume.getSeasons())
                .timezone(perfume.getTimezone())
                .longevity(perfume.getLongevity())
                .sillage(perfume.getSillage())
                .wishCount(wishCount)
                .haveCount(haveCount)
                .build();

        data.put("perfume",perfumeViewDto);
        Optional<UserEntity> userEntity = userRepository.findByUserId(dedocdId);
        if(userEntity.isPresent()){
            UserEntity user = userEntity.get();
            SurveyEntity surveyEntity = SurveyEntity.builder()
                    .user(user)
                    .perfume(perfume)
                    .likeSeasons(surveyDto.getSeason())
                    .likeGender(surveyDto.getGender())
                    .likeLongevity(surveyDto.getLongevity())
                    .likeTimezone("day")
                    .likeAccordClass(accordClass)
                    .build();

            surveyRepository.save(surveyEntity);
        }
        return data;
    }
}