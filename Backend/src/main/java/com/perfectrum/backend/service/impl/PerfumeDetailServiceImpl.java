package com.perfectrum.backend.service.impl;

import com.perfectrum.backend.dto.perfume.AccordInfoDto;
import com.perfectrum.backend.dto.perfume.AccordMoreInfoDto;
import com.perfectrum.backend.dto.perfume.PerfumeAccordsDto;
import com.perfectrum.backend.dto.perfume.PerfumeViewDto;
import com.perfectrum.backend.dto.review.ReviewListDto;
import com.perfectrum.backend.dto.review.ReviewRegistDto;
import com.perfectrum.backend.dto.review.ReviewViewDto;
import com.perfectrum.backend.domain.entity.*;
import com.perfectrum.backend.domain.repository.*;
import com.perfectrum.backend.service.PerfumeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.expression.spel.ast.OpInc;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


@Service
public class PerfumeDetailServiceImpl implements PerfumeDetailService {

    private UserRepository userRepository;
    private PerfumeRepository perfumeRepository;
    private AccordClassRepository accordClassRepository;

    private ReviewRepository reviewRepository;

    private UserDetailLogRepository userDetailLogRepository;
    private HaveListRepository haveListRepository;
    private WishListRepository wishListRepository;
    private UserAccordClassRepository userAccordClassRepository;

    private AccordRepository accordRepository;

    @Autowired
    PerfumeDetailServiceImpl(UserRepository userRepository, PerfumeRepository perfumeRepository,
                             AccordClassRepository accordClassRepository, ReviewRepository reviewRepository,
                             UserDetailLogRepository userDetailLogRepository, UserAccordClassRepository userAccordClassRepository,
                             HaveListRepository haveListRepository, WishListRepository wishListRepository,
                             AccordRepository accordRepository) {
        this.userRepository = userRepository;
        this.perfumeRepository = perfumeRepository;
        this.accordClassRepository = accordClassRepository;
        this.reviewRepository = reviewRepository;
        this.userDetailLogRepository = userDetailLogRepository;
        this.haveListRepository = haveListRepository;
        this.wishListRepository = wishListRepository;
        this.userAccordClassRepository = userAccordClassRepository;
        this.accordRepository = accordRepository;
    }

    @Override
    public Map<String, Object> getPerfumeDetail(String decodeId, Integer perfumeIdx, ReviewListDto reviewListDto) {
        Map<String, Object> data = new HashMap<>();
        Optional<UserEntity> OptionalUser = userRepository.findByUserId(decodeId);
        List<AccordEntity> accordList;


        String type = reviewListDto.getType();
        Integer lastIdx = reviewListDto.getLastIdx();
        Integer lastTotalScore = reviewListDto.getLastTotalScore();
        Integer lastLikeCount = reviewListDto.getLastLikeCount();
        Integer pageSize = reviewListDto.getPageSize();

        Pageable pageable = Pageable.ofSize(pageSize);

        PerfumeEntity perfume = perfumeRepository.findByIdx(perfumeIdx);
        accordList = perfumeRepository.findByPerfume(perfume);

        Integer haveCount = Long.valueOf(Optional.ofNullable(haveListRepository.countByPerfumeIdxAndIsDelete(perfumeIdx,false)).orElse(0L)).intValue();
        Integer wishCount = Long.valueOf(Optional.ofNullable(wishListRepository.countByPerfumeIdxAndIsDelete(perfumeIdx,false)).orElse(0L)).intValue();

        PerfumeViewDto perfumeViewDto = PerfumeViewDto.builder()
                .idx(perfumeIdx)
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
        data.put("perfume", perfumeViewDto);


        data.put("list","null");
        if(OptionalUser.isPresent()){
            UserEntity user = OptionalUser.get();
            Integer isWish = Long.valueOf(Optional.ofNullable(wishListRepository.countByUserAndPerfumeAndIsDelete(user,perfume,false)).orElse(0L)).intValue();
            if(isWish==1) {
                data.put("list", "wish");
            }else if(isWish == 0){
                Integer isHave = Long.valueOf(Optional.ofNullable(haveListRepository.countByUserAndPerfumeAndIsDelete(user,perfume,false)).orElse(0L)).intValue();
                if(isHave == 1){
                    data.put("list","have");
                }
            }

        }
        System.out.println(data.get("list")+" 뭐냐");

//        List<AccordClassEntity> aList = new ArrayList<>();

        List<AccordInfoDto> aList = new ArrayList<>();
        for(AccordEntity ae : accordList) {
            AccordInfoDto aid = AccordInfoDto.builder()
                    .accordIdx(ae.getIdx())
                    .accordName(ae.getAccordName())
                    .build();
            aList.add(aid);
        }
        data.put("perfumeAccordList", aList);
        
        Slice<ReviewEntity> reviews = reviewRepository.findByPerfumeAndIsDelete(perfume, false);

        if (!reviews.isEmpty()) {
            if (lastIdx == null) {
                lastIdx = reviewRepository.findTop1ByPerfumeOrderByIdxDesc(perfume).getIdx() + 1;
                lastTotalScore = reviewRepository.findTop1ByPerfumeOrderByIdxDesc(perfume).getIdx() + 1;
                lastLikeCount = reviewRepository.findTop1ByPerfumeOrderByIdxDesc(perfume).getIdx() + 1;
            }

            if(type==null) type= "최신순";
            if (type.equals("평점순")) {
                reviews = reviewRepository.findByPerfumeAndIsDeleteOrderTotalScoreDescIdxDesc(perfume,false, lastIdx, lastTotalScore, pageable);
            } else if (type.equals("최신순")) {
                reviews = reviewRepository.findByPerfumeAndIsDeleteOrderByIdxDesc(perfume,false, lastIdx, pageable);
            } else { // 공감순
                reviews = reviewRepository.findByPerfumeAndIsDeleteOrderByLikeCountDescIdxDesc(perfume,false, lastIdx, lastLikeCount, pageable);
            }
        }
        boolean hasNext = reviews.hasNext();
        data.put("hasNext", hasNext);

        List<ReviewViewDto> reviewList = new ArrayList<>();
        for (ReviewEntity re : reviews) {
            ReviewViewDto reviewViewDto = ReviewViewDto.builder()
                    .idx(re.getIdx())
                    .userNickname(re.getUser().getNickname())
                    .userProfileimg(re.getUser().getProfileImg())
                    .perfumeName(re.getPerfume().getPerfumeName())
                    .reviewImg(re.getReviewImg())
                    .totalScore(re.getTotalScore())
                    .longevity(re.getLongevity())
                    .sillageScore(re.getSillageScore())
                    .content(re.getContent())
                    .likeCount(re.getLikeCount())
                    .time(re.getTime())
                    .updateTime(re.getUpdateTime())
                    .isDelete(false)
                    .build();
            reviewList.add(reviewViewDto);
        }
        data.put("reviewList", reviewList);

        Optional<UserEntity> user = userRepository.findByUserId(decodeId);
        if(user.isPresent()){
            UserDetailLogEntity userDetailLogEntity = new UserDetailLogEntity().builder()
                    .user(user.get())
                    .perfume(perfume)
                    .build();
            userDetailLogRepository.save(userDetailLogEntity);
        }
        return data;
    }

    @Override
    public Map<String,Object> getAcoordMoreInfo(Integer accordIdx){
        Map<String,Object> data = new HashMap<>();
        AccordEntity ae = accordRepository.findByIdx(accordIdx);

        AccordMoreInfoDto accordMoreInfoDto = AccordMoreInfoDto.builder()
                .accordName(ae.getAccordName())
                .accordImg(ae.getAccordImg())
                .accordDescription(ae.getAccordDescription())
                .build();

        data.put("accordMoreInfo",accordMoreInfoDto);

        return data;

    }

    @Override
    public Map<String, Object> addWishList(String decodeId, Integer perfumeIdx) {
        Optional<UserEntity> userOptional = userRepository.findByUserId(decodeId);
        Map<String, Object> result = new HashMap<>();

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            PerfumeEntity perfume = perfumeRepository.findByIdx(perfumeIdx);

            Optional<WishListEntity> wishListOptional = wishListRepository.findByUserAndPerfume(user, perfume);
            if (wishListOptional.isPresent()) {
                WishListEntity wishList = wishListOptional.get();
                if(wishList.getIsDelete()==false){
                    WishListEntity wishListEntity = WishListEntity.builder()
                            .idx(wishList.getIdx())
                            .user(user)
                            .perfume(perfume)
                            .isDelete(true)
                            .build();
                    wishListRepository.save(wishListEntity);

                    List<AccordClassEntity> accordClassEntities = accordClassRepository.findByPerfumeAccordClass(perfume);
                    for (AccordClassEntity a : accordClassEntities) {
                        Optional<UserAccordClassEntity> userAccordClass = userAccordClassRepository.findByUserAndAccordClass(user, a);
                        if (userAccordClass.isPresent()) {
                            UserAccordClassEntity updateUserAccordClass = UserAccordClassEntity.builder()
                                    .idx(userAccordClass.get().getIdx())
                                    .user(userAccordClass.get().getUser())
                                    .accordClass(userAccordClass.get().getAccordClass())
                                    .accordClassCount(userAccordClass.get().getAccordClassCount() - 1)
                                    .build();

                            userAccordClassRepository.save(updateUserAccordClass);
                        }
                    }
                    result.put("isClicked", "false");
                }else{
                    WishListEntity wishListEntity = WishListEntity.builder()
                            .idx(wishList.getIdx())
                            .user(user)
                            .perfume(perfume)
                            .isDelete(false)
                            .build();
                    wishListRepository.save(wishListEntity);
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
                    result.put("isClicked", "true");
                }

            } else { // 없음 -> db에 등록
                WishListEntity wishList = WishListEntity.builder()
                        .user(user)
                        .perfume(perfume)
                        .build();
                wishListRepository.save(wishList);

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
                result.put("isClicked", "true");
            }
        }
        return result;
    }

    @Override
    public Map<String, Object> addHaveList(String decodeId, Integer perfumeIdx) {
        Map<String, Object> result = new HashMap<>();
        Optional<UserEntity> userOptional = userRepository.findByUserId(decodeId);
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
                result.put("isWishClicked", "false");
                result.put("isClicked", "true");
            } else { // 없음 -> 바로 DB 등록
                Optional<HaveListEntity> haveListOptional = haveListRepository.findByUserAndPerfume(user, perfume);

                if (haveListOptional.isPresent()) {
                    if(haveListOptional.get().getIsDelete()==false){
                        HaveListEntity haveList = HaveListEntity.builder()
                                .idx(haveListOptional.get().getIdx())
                                .user(user)
                                .perfume(perfume)
                                .isDelete(true)
                                .build();
                        haveListRepository.save(haveList);
                        List<AccordClassEntity> accordClassEntities = accordClassRepository.findByPerfumeAccordClass(perfume);
                        for (AccordClassEntity a : accordClassEntities) {
                            Optional<UserAccordClassEntity> userAccordClass = userAccordClassRepository.findByUserAndAccordClass(user, a);
                            if (userAccordClass.isPresent()) {
                                UserAccordClassEntity updateUserAccordClass = UserAccordClassEntity.builder()
                                        .idx(userAccordClass.get().getIdx())
                                        .user(userAccordClass.get().getUser())
                                        .accordClass(userAccordClass.get().getAccordClass())
                                        .accordClassCount(userAccordClass.get().getAccordClassCount() - 1)
                                        .build();

                                userAccordClassRepository.save(updateUserAccordClass);
                            }
                        }
                        result.put("isClicked", "false");
                    }else{
                        HaveListEntity haveList = HaveListEntity.builder()
                                .idx(haveListOptional.get().getIdx())
                                .user(user)
                                .perfume(perfume)
                                .isDelete(false)
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
                        result.put("isClicked", "true");
                    }
                } else {
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
                    result.put("isClicked", "true");
                }
            }
        }
        return result;
    }

    @Override
    public void registReview(String decodeId, Integer perfumeIdx, ReviewRegistDto reviewRegistDto) {
        Optional<UserEntity> user = userRepository.findByUserId(decodeId);
        PerfumeEntity perfume = perfumeRepository.findByIdx(perfumeIdx);
        String reviewImg;
        if(reviewRegistDto.getReviewImg()==null){
            reviewImg = perfumeRepository.findByIdx(perfumeIdx).getPerfumeImg();
        }else {
            reviewImg = reviewRegistDto.getReviewImg();
        }

        if (user.isPresent()) {
            ReviewEntity reviewEntity = ReviewEntity.builder()
                    .user(user.get())
                    .perfume(perfume)
                    .reviewImg(reviewImg)
                    .totalScore(reviewRegistDto.getTotalScore())
                    .longevity(reviewRegistDto.getLongevity())
                    .sillageScore(reviewRegistDto.getSillageScore())
                    .content(reviewRegistDto.getContent())
                    .updateTime(null)
                    .isDelete(false)
                    .build();
            reviewRepository.save(reviewEntity);
        }
    }

    @Override
    public void updateReview(String decodeId, Integer perfumeIdx, Integer reviewIdx, ReviewRegistDto reviewRegistDto) {
        Optional<UserEntity> user = userRepository.findByUserId(decodeId);
        ReviewEntity originReview = reviewRepository.findByIdx(reviewIdx);
        String reviewImg = reviewRegistDto.getReviewImg();
        if(reviewImg==null){
            reviewImg = perfumeRepository.findByIdx(perfumeIdx).getPerfumeImg();
        }
        if (user.isPresent()) {
            ReviewEntity reviewEntity = ReviewEntity.builder()
                    .idx(originReview.getIdx())
                    .user(originReview.getUser())
                    .perfume(originReview.getPerfume())
                    .reviewImg(reviewImg)
                    .totalScore(reviewRegistDto.getTotalScore())
                    .longevity(reviewRegistDto.getLongevity())
                    .sillageScore(reviewRegistDto.getSillageScore())
                    .content(reviewRegistDto.getContent())
                    .likeCount(originReview.getLikeCount())
                    .time(originReview.getTime())
                    .updateTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                    .isDelete(originReview.getIsDelete())
                    .build();

            reviewRepository.save(reviewEntity);
        }

    }

    @Override
    public void deleteReview(String decodeId,Integer perfumeIdx,Integer reviewIdx){
        Optional<UserEntity> user = userRepository.findByUserId(decodeId);
        PerfumeEntity perfume = perfumeRepository.findByIdx(perfumeIdx);
        ReviewEntity review = reviewRepository.findByIdx(reviewIdx);
        if(user.isPresent()){
            ReviewEntity reviewEntity = ReviewEntity.builder()
                    .idx(review.getIdx())
                    .user(user.get())
                    .perfume(perfume)
                    .reviewImg(review.getReviewImg())
                    .totalScore(review.getTotalScore())
                    .longevity(review.getLongevity())
                    .sillageScore(review.getSillageScore())
                    .content(review.getContent())
                    .likeCount(review.getLikeCount())
                    .time(review.getTime())
                    .updateTime(review.getUpdateTime())
                    .isDelete(!review.getIsDelete())
                    .build();

            reviewRepository.save(reviewEntity);
        }
    }

    @Override
    public void clickLike(String decodeId,Integer perfumeIdx,Integer reviewIdx){
        Optional<UserEntity> user = userRepository.findByUserId(decodeId);
        PerfumeEntity perfume = perfumeRepository.findByIdx(perfumeIdx);
        ReviewEntity review = reviewRepository.findByIdx(reviewIdx);
        if(!review.getIsDelete()) {
            if (user.isPresent()) {
                ReviewEntity reviewEntity = ReviewEntity.builder()
                        .idx(review.getIdx())
                        .user(user.get())
                        .perfume(perfume)
                        .reviewImg(review.getReviewImg())
                        .totalScore(review.getTotalScore())
                        .longevity(review.getLongevity())
                        .sillageScore(review.getSillageScore())
                        .content(review.getContent())
                        .likeCount(review.getLikeCount() + 1)
                        .time(review.getTime())
                        .updateTime(review.getUpdateTime())
                        .isDelete(review.getIsDelete())
                        .build();

                reviewRepository.save(reviewEntity);
            }
        }
    }

    @Override
    public void unclickLike(String decodeId,Integer perfumeIdx,Integer reviewIdx){
        Optional<UserEntity> user = userRepository.findByUserId(decodeId);
        PerfumeEntity perfume = perfumeRepository.findByIdx(perfumeIdx);
        ReviewEntity review = reviewRepository.findByIdx(reviewIdx);
        if(!review.getIsDelete()) {
            if (user.isPresent()) {
                ReviewEntity reviewEntity = ReviewEntity.builder()
                        .idx(review.getIdx())
                        .user(user.get())
                        .perfume(perfume)
                        .reviewImg(review.getReviewImg())
                        .totalScore(review.getTotalScore())
                        .longevity(review.getLongevity())
                        .sillageScore(review.getSillageScore())
                        .content(review.getContent())
                        .likeCount(review.getLikeCount() - 1)
                        .time(review.getTime())
                        .updateTime(review.getUpdateTime())
                        .isDelete(review.getIsDelete())
                        .build();

                reviewRepository.save(reviewEntity);
            }
        }
    }
}
