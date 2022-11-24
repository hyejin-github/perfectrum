package com.perfectrum.backend.service.impl;

import com.perfectrum.backend.domain.entity.AccordClassEntity;
import com.perfectrum.backend.domain.entity.PerfumeEntity;
import com.perfectrum.backend.domain.entity.UserEntity;
import com.perfectrum.backend.domain.repository.AccordClassRepository;
import com.perfectrum.backend.domain.repository.PerfumeRepository;
import com.perfectrum.backend.domain.repository.UserRepository;
import com.perfectrum.backend.dto.perfume.PerfumeViewDto;
import com.perfectrum.backend.dto.user.UserInfoDto;
import com.perfectrum.backend.mapper.PerfumeViewMapper;
import com.perfectrum.backend.service.PerfumeService;
import com.perfectrum.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class PerfumeServiceImpl implements PerfumeService {
    private PerfumeRepository perfumeRepository;
    private PerfumeViewMapper perfumeViewMapper;
    private UserRepository userRepository;
    private AccordClassRepository accordClassRepository;


    @Autowired
    PerfumeServiceImpl(PerfumeRepository perfumeRepository, PerfumeViewMapper perfumeViewMapper, UserRepository userRepository,
                       AccordClassRepository accordClassRepository){
        this.perfumeRepository = perfumeRepository;
        this.perfumeViewMapper = perfumeViewMapper;
        this.userRepository = userRepository;
        this.accordClassRepository = accordClassRepository;
    }
    @Override
    public List<PerfumeViewDto> viewBestPerfume(String decodeId) {
        List<PerfumeViewDto> result = new ArrayList<>();
        Optional<UserEntity> userEntityOptional = userRepository.findByUserId(decodeId);

        if(userEntityOptional.isPresent()){
            UserEntity userEntity = userEntityOptional.get();
            String gender = userEntity.getGender();
            String season = userEntity.getSeasons();
            Integer accordClass = userEntity.getAccordClass();
            // 추가 정보 있음 -> 정보 기반 추천
            if(gender != null){
                AccordClassEntity accordClassEntity = accordClassRepository.findByIdx(accordClass);
                Pageable top6 = PageRequest.ofSize(6);
                List<PerfumeEntity> perfumes = perfumeRepository.findBest6Perfumes(gender, season, accordClassEntity,top6);

                for(PerfumeEntity p : perfumes){
                    PerfumeViewDto perfumeViewDto = perfumeViewMapper.toDto(p);
                    result.add(perfumeViewDto);
                }
            }else{ // 추가정보 없음 -> 기본 베스트 향수 추천
                List<PerfumeEntity> perfumes = perfumeRepository.findTop6ByOrderByItemRatingDesc();

                for(PerfumeEntity p : perfumes){
                    PerfumeViewDto perfumeViewDto = perfumeViewMapper.toDto(p);
                    result.add(perfumeViewDto);
                }
            }
        }else{ // 비로그인 상태
            List<PerfumeEntity> perfumes = perfumeRepository.findTop6ByOrderByItemRatingDesc();
            for(PerfumeEntity p : perfumes){
                PerfumeViewDto perfumeViewDto = perfumeViewMapper.toDto(p);
                result.add(perfumeViewDto);
            }
        }
        return result;
    }

    @Override
    public List<PerfumeViewDto> viewTodayPerfume(String decodeId) {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        int monthValue = now.getMonthValue();
        // 계절
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

        // 2. 시간 얻어오기
        LocalTime nowTIme = LocalTime.now(ZoneId.of("Asia/Seoul"));
        int hour = nowTIme.getHour();

        // 시간대 구하기
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
            result.add(list.get(num[i]));
        }

        return result;
    }
}
