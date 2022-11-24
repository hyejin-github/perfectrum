package com.perfectrum.backend.service;

import com.perfectrum.backend.domain.entity.AccordClassEntity;
import com.perfectrum.backend.domain.entity.PerfumeEntity;
import com.perfectrum.backend.domain.entity.SurveyEntity;
import com.perfectrum.backend.domain.entity.UserEntity;
import com.perfectrum.backend.domain.repository.*;
import com.perfectrum.backend.mapper.PerfumeViewMapper;
//import org.junit.Test;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.*;


@SpringBootTest
@Transactional
public class SurveyServiceTest {
    private static final String success = "SUCCESS";
    private static final String fail = "FAIL";
    private static final String timeOut = "access-token timeout";
    private UserRepository userRepository;
    private SurveyRepository surveyRepository;
    private PerfumeRepository perfumeRepository;
    private AccordClassRepository accordClassRepository;
    private AccordRepository accordRepository;

    @Autowired
    SurveyServiceTest(UserRepository userRepository, PerfumeRepository perfumeRepository, SurveyRepository surveyRepository,
                      AccordClassRepository accordClassRepository, AccordRepository accordRepository){
        this.userRepository = userRepository;
        this.perfumeRepository = perfumeRepository;
        this.surveyRepository = surveyRepository;
        this.accordClassRepository = accordClassRepository;
        this.accordRepository = accordRepository;
    }

    @Test
    public void 설문조사_테스트(){
        // given
        String gender = "남자"; // 남자, 여자, 상관없음
        String season = "가을";
        String accordClass_s = "꽃 향기";
        Integer duration = 0;
        Integer accordClass = 0;

        if(gender.equals("남자")){
            gender = "Men";
        }else if(gender.equals("여자")){
            gender = "Women";
        }else{
            gender = null;
        }

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

        // when
        AccordClassEntity accordClassEntity = accordClassRepository.findByIdx(accordClass);
        List<PerfumeEntity> surveyList = new ArrayList<>();
        if(duration==0){ // 1,2,3
            if(gender==null){
                surveyList = perfumeRepository.findBySeasonAndWeakLongevityAndAccordClass(season, accordClassEntity);
            }else{
                surveyList = perfumeRepository.findByGenderAndSeasonAndWeakLongevityAndAccordClass(gender, season, accordClassEntity);
            }
        }else{ // 4,5
            if(gender==null){
                surveyList = perfumeRepository.findBySeasonAndStrongLongevityAndAccordClass(season, accordClassEntity);
            }else{
                surveyList = perfumeRepository.findByGenderAndSeasonAndStrongLongevityAndAccordClass(gender, season, accordClassEntity);
            }
        }

        Integer max=0, cnt = 0;
        ArrayList<PerfumeEntity> resultList = new ArrayList<>();
        for(int i=0; i<surveyList.size(); i++){
            cnt = accordRepository.countByAccordClass(surveyList.get(i), accordClassEntity);
//            System.out.println( surveyList.get(i).getIdx()+"번째 향수 accordClass포함 개수 : " + cnt);

            if(max < cnt){
                max = cnt;
                resultList.clear();
                resultList.add(surveyList.get(i));
            }else if(max == cnt){
                resultList.add(surveyList.get(i));
            }
        }


        Random random = new Random();
        PerfumeEntity perfume;
        if(resultList.size() != 1){
            perfume = resultList.get(random.nextInt(resultList.size()));

        }else {
            perfume = resultList.get(0);
        }


        // then
        if(!resultList.isEmpty()){
            System.out.println("survey결과 총 개수 : " + resultList.size());
            for(PerfumeEntity p : resultList){
                System.out.println(p.getIdx()+"번향수 ,"+p.getPerfumeName());
            }
        }else{
            System.out.println("쿼리 검색 결과 없음");
        }
        System.out.println("최종 결과 : " + perfume.getIdx()+ ", " + perfume.getPerfumeName());
    }
}