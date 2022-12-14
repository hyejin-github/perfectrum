package com.perfectrum.backend.service;

import com.perfectrum.backend.domain.entity.AccordClassEntity;
import com.perfectrum.backend.domain.entity.AccordEntity;
import com.perfectrum.backend.domain.entity.PerfumeEntity;
import com.perfectrum.backend.domain.entity.UserEntity;
import com.perfectrum.backend.domain.repository.AccordClassRepository;
import com.perfectrum.backend.domain.repository.AccordRepository;
import com.perfectrum.backend.domain.repository.PerfumeRepository;
import com.perfectrum.backend.domain.repository.UserRepository;
import com.perfectrum.backend.dto.perfume.AccordMoreInfoDto;
import com.perfectrum.backend.dto.perfume.PerfumeViewDto;
import com.perfectrum.backend.mapper.PerfumeViewMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SpringBootTest
@Transactional
public class MainServiceTest {
    private static final String success = "SUCCESS";
    private static final String fail = "FAIL";
    private static final String timeOut = "access-token timeout";
    private static HttpStatus status = HttpStatus.NOT_FOUND;
    private UserRepository userRepository;
    private PerfumeRepository perfumeRepository;
    private PerfumeViewMapper perfumeViewMapper;
    private AccordClassRepository accordClassRepository;

    private JwtService jwtService;
    private AccordRepository accordRepository;


    @Autowired
    MainServiceTest(UserRepository userRepository, PerfumeRepository perfumeRepository, PerfumeViewMapper perfumeViewMapper, JwtService jwtService,
                    AccordClassRepository accordClassRepository, AccordRepository accordRepository){
        this.userRepository = userRepository;
        this.perfumeRepository = perfumeRepository;
        this.perfumeViewMapper = perfumeViewMapper;
        this.accordClassRepository = accordClassRepository;
        this.jwtService = jwtService;
        this.accordRepository = accordRepository;
    }

    @Test
    public void ????????????_?????????_?????????(){
        // given
        Integer idx = 1;

        // when
        List<AccordMoreInfoDto> dto = new ArrayList<>();
        AccordClassEntity accordClassEntity = accordClassRepository.findByIdx(idx);
        List<AccordEntity> entityList = accordRepository.findByAccordClass(accordClassEntity);

        if(!entityList.isEmpty()){
            for(AccordEntity ac : entityList){
                AccordMoreInfoDto accord = AccordMoreInfoDto.builder()
                        .accordName(ac.getAccordName())
                        .accordImg(ac.getAccordImg())
                        .accordDescription(ac.getAccordDescription())
                        .build();

                dto.add(accord);
            }
        }

        for(AccordMoreInfoDto a : dto){
            System.out.println(a.toString());
        }
    }
    @Test
    public void ?????????_??????_top6_??????() {
        // given
        // ?????? ?????? ?????? ???
//        String testId = "kakao123145";
        String testId = "kakao123456";
        String gender = null;
        String season = null;
        Integer accordClass;

        // ?????? ?????? ?????? ???

        // when
        Map<String, Object> resultMap = new HashMap<>();
        List<PerfumeViewDto> result = new ArrayList<>();

        Optional<UserEntity> testUserOptional = userRepository.findByUserId(testId);
        // ????????? ??? ??????
        if(testUserOptional.isPresent()){
            UserEntity testUser = testUserOptional.get();
            gender = testUser.getGender();
            season = testUser.getSeasons();
            accordClass = testUser.getAccordClass();
            System.out.println("===================================");
            System.out.println(gender + " , " + season + ", " + accordClass);
            System.out.println("===================================");
            // ?????? ?????? ?????? ????????? -> ?????? ?????? ?????? ?????? ??????
            if(gender != null){
//                List<PerfumeEntity> perfumes = perfumeRepository.findTop6ByGenderAndSeasonsContainsOrderByItemRatingDesc(gender, season);
                AccordClassEntity accordClassEntity = accordClassRepository.findByIdx(accordClass);
                Pageable top6 = PageRequest.ofSize(6);
                List<PerfumeEntity> perfumes = perfumeRepository.findBest6Perfumes(gender, season, accordClassEntity,top6);
                for(PerfumeEntity p : perfumes){
                    PerfumeViewDto perfumeViewDto = perfumeViewMapper.toDto(p);
                    result.add(perfumeViewDto);
                }

                resultMap.put("data", result);
                resultMap.put("message", success);
                status = HttpStatus.OK;
            }else{
                // ?????? ?????? ?????? -> ?????? ????????? ?????? ??????
                List<PerfumeEntity> perfumes = perfumeRepository.findTop6ByOrderByItemRatingDesc();
                for(PerfumeEntity p : perfumes){
                    PerfumeViewDto perfumeViewDto = perfumeViewMapper.toDto(p);
                    result.add(perfumeViewDto);
                }

                resultMap.put("data", result);
                resultMap.put("message", success);
                status = HttpStatus.OK;
//                for(PerfumeViewDto d : result){
//                    System.out.println(d.toString());
//                }
            }
        }else { // ????????? ?????? ?????? -> ?????? ????????? ?????? ??????
            List<PerfumeEntity> perfumes = perfumeRepository.findTop6ByOrderByItemRatingDesc();
            for(PerfumeEntity p : perfumes){
                PerfumeViewDto perfumeViewDto = perfumeViewMapper.toDto(p);
                result.add(perfumeViewDto);
            }

            resultMap.put("data", result);
            resultMap.put("message", success);
            status = HttpStatus.OK;
        }

        // then
        for (PerfumeViewDto d : result){
            System.out.println(d.toString());
        }
        System.out.println(resultMap.toString());
    }

    @Test
    public void ?????????_??????_Top6_??????() {
        // ?????????, ???????????? ?????? -> ?????? ??????(???) => season / ?????? ?????? => timezone?????? ???????????? ??????
        // given
        // 1. ?????? ????????????
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));

        // ?????? ??? ????????????
        String monthString = now.getMonth().toString();
        int monthValue = now.getMonthValue();

        System.out.println("========= Month =========");
        System.out.println(monthString);
        System.out.println(monthValue+"???");
        System.out.println("==========================");

        // ??????
        String season;
        switch (monthValue){
            case 3:
            case 4:
            case 5:
                season = "spring";
                break;
            case 6:
            case 7:
            case 8:
                season = "summer";
                break;
            case 9:
            case 10:
            case 11:
                season = "fall";
                break;
            default:
                season = "winter";
                break;
        }

        System.out.println("========== season ==========");
        System.out.println(season);
        System.out.println("==========================");

        // 2. ?????? ????????????
        LocalTime nowTIme = LocalTime.now(ZoneId.of("Asia/Seoul"));
        int hour = nowTIme.getHour();

        System.out.println("========== Hour ==========");
        System.out.println(hour);
        System.out.println("==========================");

        // ????????? ?????????
        String timeZone;
        if(6<=hour && hour<=18) timeZone = "day";
        else timeZone = "night";

        System.out.println("========== Timezone ==========");
        System.out.println(timeZone);
        System.out.println("==========================");

        // when
        Map<String, Object> resultMap = new HashMap<>();
        List<PerfumeViewDto> result = new ArrayList<>();

//        List<PerfumeEntity> perfumeEntities = perfumeRepository.findTop20ByTimezoneAndSeasonsContainsOrderByItemRatingDesc(timeZone, season);
        List<PerfumeEntity> perfumeEntities = perfumeRepository.findTop6ByTimezoneAndSeasonsContainsOrderByItemRatingDesc(timeZone, season);
        for(PerfumeEntity p : perfumeEntities){
            PerfumeViewDto dto = perfumeViewMapper.toDto(p);
            result.add(dto);
        }

        resultMap.put("data", result);
        resultMap.put("message", success);
        status = HttpStatus.OK;

        // then
        for(PerfumeViewDto d : result){
            System.out.println(d.toString());
        }
    }

    @Test
    public void ?????????_??????_Top20_??????6???_??????() {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        int monthValue = now.getMonthValue();
        // ??????
        String season;
        switch (monthValue){
            case 3:
            case 4:
            case 5:
                season = "spring";
                break;
            case 6:
            case 7:
            case 8:
                season = "summer";
                break;
            case 9:
            case 10:
            case 11:
                season = "fall";
                break;
            default:
                season = "winter";
                break;
        }

        // 2. ?????? ????????????
        LocalTime nowTIme = LocalTime.now(ZoneId.of("Asia/Seoul"));
        int hour = nowTIme.getHour();

        // ????????? ?????????
        String timeZone;
        if(6<=hour && hour<=18) timeZone = "day";
        else timeZone = "night";

        // when
        Map<String, Object> resultMap = new HashMap<>();
        List<PerfumeViewDto> list = new ArrayList<>();
        List<PerfumeViewDto> result = new ArrayList<>();

        List<PerfumeEntity> perfumeEntities = perfumeRepository.findTop20ByTimezoneAndSeasonsContainsOrderByItemRatingDesc(timeZone, season);
        for(PerfumeEntity p : perfumeEntities){
            PerfumeViewDto dto = perfumeViewMapper.toDto(p);
            list.add(dto);
        }

        int[] num = new int[6];
        Random r = new Random();
        for(int i=0; i<6; i++){
            num[i] = r.nextInt(list.size());
            for(int j=0; j<i; j++){
                if(num[i] == num[j]) i--;
            }
        }

        for(int i=0; i<6; i++){
            System.out.println(num[i]);
            result.add(list.get(num[i]));
        }
        resultMap.put("data", result);
        resultMap.put("message", success);
        status = HttpStatus.OK;

        // then
        for(PerfumeViewDto d : result){
            System.out.println(d.toString());
        }
    }

    @Test
    public void ????????????_???????????????_??????_?????????(){
        String accessToken = "eyJ0eXAiOiJKV1QiLCJyZWdEYXRlIjoxNjYzNjMwNzI5NjUwLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NjM2MzQzMjksInN1YiI6ImFjY2Vzcy10b2tlbiIsImlkIjoia2FrYW8yNDM1NTc3MTg0In0.Iuy5rwZ0Xrn7Mfhyg9moaO7Z5R_YwAT-eDPPl0UaagQ";
        String decodeId = jwtService.decodeToken(accessToken);
        System.out.println(decodeId);
    }
}
