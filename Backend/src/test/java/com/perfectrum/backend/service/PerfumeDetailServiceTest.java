package com.perfectrum.backend.service;


import com.perfectrum.backend.domain.entity.*;
import com.perfectrum.backend.domain.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
@Transactional
public class PerfumeDetailServiceTest {
    private static final String success = "SUCCESS";
    private static final String fail = "FAIL";
    private static final String timeOut = "access-token timeout";

    private UserRepository userRepository;
    private PerfumeRepository perfumeRepository;
    private AccordClassRepository accordClassRepository;

    private ReviewRepository reviewRepository;

    private UserDetailLogRepository userDetailLogRepository;
    private HaveListRepository haveListRepository;
    private WishListRepository wishListRepository;
    private UserAccordClassRepository userAccordClassRepository;

    @Autowired
    PerfumeDetailServiceTest(UserRepository userRepository, PerfumeRepository perfumeRepository,
                             AccordClassRepository accordClassRepository, ReviewRepository reviewRepository,
                             UserDetailLogRepository userDetailLogRepository,
                             HaveListRepository haveListRepository, WishListRepository wishListRepository, UserAccordClassRepository userAccordClassRepository) {
        this.userRepository = userRepository;
        this.perfumeRepository = perfumeRepository;
        this.accordClassRepository = accordClassRepository;
        this.reviewRepository = reviewRepository;
        this.userDetailLogRepository = userDetailLogRepository;
        this.haveListRepository = haveListRepository;
        this.wishListRepository = wishListRepository;
        this.userAccordClassRepository = userAccordClassRepository;
    }

    @Test
    public void 향수상세정보_테스트() {
        Integer userIdx = 10;
        String userId = "Test";
        Integer perfumeIdx = 100;

        Optional<UserEntity> tmpUser = userRepository.findByUserId(userId);
        Map<String, Object> resultMap = new HashMap<>();

        PerfumeEntity perfume = perfumeRepository.findByIdx(perfumeIdx);
        List<ReviewEntity> review_list = reviewRepository.findByPerfumeIdx(perfume.getIdx());

        resultMap.put("reviewList", review_list);
        resultMap.put("perfume", perfume);
        resultMap.put("message", success);
        if (tmpUser.isPresent()) {


            UserDetailLogEntity userDetailLogEntity = UserDetailLogEntity.builder()
                    .user(tmpUser.get())
                    .perfume(perfume)
                    .build();

            userDetailLogRepository.save(userDetailLogEntity);
        }
    }

    @Test
    public void 위시리스트_향수담기() {
        String testId = "kakao123145";
        Integer perfumeIdx = 471;

        Optional<UserEntity> userOptional = userRepository.findByUserId(testId);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            PerfumeEntity perfume = perfumeRepository.findByIdx(perfumeIdx);

            Optional<WishListEntity> wishListOptional = wishListRepository.findByUserAndPerfumeAndIsDelete(user, perfume, false);
//            System.out.println(cnt);
            // 이미 등록한 향수 -> 삭제 처리
            if (wishListOptional.isPresent()) {
                WishListEntity wishList = WishListEntity.builder()
                        .idx(wishListOptional.get().getIdx())
                        .user(user)
                        .perfume(perfume)
                        .isDelete(true)
                        .build();
                wishListRepository.save(wishList);
            } else { // 없음 -> db에 등록
                WishListEntity wishList = WishListEntity.builder()
                        .user(user)
                        .perfume(perfume)
                        .build();
                wishListRepository.save(wishList);
            }
        }
    }

    @Test
    public void 보유리스트_담기() {
        String testId = "kakao123145";
        Integer perfumeIdx = 471;

        Optional<UserEntity> userOptional = userRepository.findByUserId(testId);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            PerfumeEntity perfume = perfumeRepository.findByIdx(perfumeIdx);

            Optional<WishListEntity> wishListOptional = wishListRepository.findByUserAndPerfumeAndIsDelete(user, perfume, false);

            // 이미 위시에 담겨져있음 -> 위시에서 지우고 보유에 등록
            if (wishListOptional.isPresent()) {
                WishListEntity wishList = WishListEntity.builder()
                        .idx(wishListOptional.get().getIdx())
                        .user(user)
                        .perfume(perfume)
                        .isDelete(true)
                        .build();
                wishListRepository.save(wishList);

                HaveListEntity haveList = HaveListEntity.builder()
                        .user(user)
                        .perfume(perfume)
                        .build();
                haveListRepository.save(haveList);
            } else { // 없음 -> 바로 DB 등록
                HaveListEntity haveList = HaveListEntity.builder()
                        .user(user)
                        .perfume(perfume)
                        .build();
                haveListRepository.save(haveList);

                List<AccordClassEntity> accordClassEntity = accordClassRepository.findByPerfumeAccordClass(perfume);
                for (AccordClassEntity a : accordClassEntity) {
                    Optional<UserAccordClassEntity> userAccordClass = userAccordClassRepository.findByUserAndAccordClass(user, a);
                    // DB 존재 -> cnt+1 수정
                    if (userAccordClass.isPresent()) {
                        UserAccordClassEntity updateUserAccordClass = UserAccordClassEntity.builder()
                                .idx(userAccordClass.get().getIdx())
                                .user(userAccordClass.get().getUser())
                                .accordClass(userAccordClass.get().getAccordClass())
                                .accordClassCount(userAccordClass.get().getAccordClassCount() + 1)
                                .build();
                        userAccordClassRepository.save(updateUserAccordClass);

                    } else { // DB에 삽입
                        UserAccordClassEntity userAccordClassEntity = UserAccordClassEntity.builder()
                                .user(user)
                                .accordClass(a)
                                .build();
                        userAccordClassRepository.save(userAccordClassEntity);
                    }
                }
            }
        }
    }
}
