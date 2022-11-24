package com.perfectrum.backend.service;

import com.perfectrum.backend.domain.entity.PerfumeEntity;
import com.perfectrum.backend.domain.entity.ReviewEntity;
import com.perfectrum.backend.domain.entity.UserEntity;
import com.perfectrum.backend.domain.repository.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@SpringBootTest
@Disabled
public class ReviewServiceTest {
    private static final String success = "SUCCESS";
    private static final String fail = "FAIL";
    private static final String timeOut = "access-token timeout";
    private UserRepository userRepository;
    private PerfumeRepository perfumeRepository;
    private ReviewRepository reviewRepository;
    private UserSearchLogRepository userSearchLogRepository;

    @Autowired
    ReviewServiceTest(UserRepository userRepository, PerfumeRepository perfumeRepository, ReviewRepository reviewRepository){
        this.userRepository = userRepository;
        this.perfumeRepository = perfumeRepository;
        this.reviewRepository = reviewRepository;
    }



    @Test
    public void 리뷰등록_테스트(){
        Integer userIdx = 3;
        String userId = "Test";
        Integer perfumeIdx = 100;
        String reviewImg = "ImgURL";
        Integer totalScore = 4;
        Integer longevity;
        Integer sillageScore;
        String content = "이 향수 너무 좋아요";
        Integer likeCount = 3;
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        Optional<UserEntity> tmpUser = userRepository.findByUserId(userId);
        PerfumeEntity perfume = perfumeRepository.findByIdx(perfumeIdx);
        Map<String,Object> resultMap = new HashMap<>();

        if(tmpUser.isPresent()){
            ReviewEntity reviewEntity = ReviewEntity.builder()
                    .user(tmpUser.get())
                    .perfume(perfume)
                    .reviewImg(reviewImg)
                    .totalScore(totalScore)
                    .longevity(perfume.getLongevity())
                    .sillageScore(perfume.getSillage())
                    .content(content)
                    .time(now)
                    .updateTime(null)
                    .build();
            reviewRepository.save(reviewEntity);
            resultMap.put("message",success);
        }else{

        }
    }

    @Test
    public void 리뷰수정_테스트(){
        Integer userIdx = 3;
        String userId = "Test";
        Integer perfumeIdx = 100;
        String reviewImg = "imgURL_re";
        Integer totalScore = 1;
        Integer longevity;
        Integer sillageScore;
        String content = "이 향수 별로에요";
        Integer likeCount = 3;
        Integer reviewIdx = 1038;

        Optional<UserEntity> tmpUser = userRepository.findByUserId(userId);
        PerfumeEntity perfume = perfumeRepository.findByIdx(perfumeIdx);
        Map<String,Object> resultMap = new HashMap<>();
        ReviewEntity re = reviewRepository.findByIdx(1038);

//        ReviewEntity reviewEntity = reviewRepository.findByIdx(reviewIdx);
        if(tmpUser.isPresent()){
            LocalDateTime now = LocalDateTime.now();
//            now.atOffset("Asia/Seoul");
            ReviewEntity reviewEntity = ReviewEntity.builder()
                    .idx(re.getIdx())
                    .user(tmpUser.get())
                    .perfume(perfume)
                    .reviewImg(reviewImg)
                    .totalScore(totalScore)
                    .longevity(perfume.getLongevity())
                    .sillageScore(perfume.getSillage())
                    .content(content)
                    .likeCount(6)
                    .time(re.getTime())
                    .updateTime(now)
                    .build();
            reviewRepository.save(reviewEntity);
            resultMap.put("message",success);
        }
    }
}
