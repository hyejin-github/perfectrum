package com.perfectrum.backend.service;

import com.perfectrum.backend.domain.entity.*;
import com.perfectrum.backend.domain.repository.*;
import com.perfectrum.backend.mapper.PerfumeViewMapper;
//import org.junit.Test;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@SpringBootTest
@Transactional
public class SearchServiceTest {
    private static final String success = "SUCCESS";
    private static final String fail = "FAIL";
    private static final String timeOut = "access-token timeout";
    private UserRepository userRepository;
    private PerfumeRepository perfumeRepository;

    private UserSearchLogRepository userSearchLogRepository;
    private AccordClassRepository accordClassRepository;

    @Autowired
    SearchServiceTest(UserRepository userRepository, PerfumeRepository perfumeRepository, AccordClassRepository accordClassRepository,UserSearchLogRepository userSearchLogRepository){
        this.userRepository = userRepository;
        this.perfumeRepository = perfumeRepository;
        this.accordClassRepository = accordClassRepository;
        this.userSearchLogRepository = userSearchLogRepository;
    }

    @Test
    public void 상세검색_테스트(){
        Integer user_idx = 3;
        String user_id = "searchT";
        String gender = "Men";
        Integer duration = 4;
        Integer accord_class = 1;

        Optional<UserEntity> tmpUser = userRepository.findByUserId(user_id);
        AccordClassEntity ac_Class = accordClassRepository.findByIdx(accord_class);
        Map<String,Object> resultMap = new HashMap<>();

        // 검색결과에 따른 퍼퓸 반환
        List<PerfumeEntity> perfumes = perfumeRepository.findAllByGenderAndLongevity(gender,duration);
        resultMap.put("data",perfumes);
        // 로그인 했을 경우 검색로그 저장
        if(tmpUser.isPresent()){
            UserSearchLogEntity userSearchLogEntity = UserSearchLogEntity.builder()
                    .user(tmpUser.get())
                    .gender(gender)
                    .duration(duration)
                    .accordClass(ac_Class)
                    .build();
            userSearchLogRepository.save(userSearchLogEntity);

            resultMap.put("message",success);
        }else{
            resultMap.put("message",success);

        }
        System.out.println(perfumes.size());
    }
}