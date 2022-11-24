package com.perfectrum.backend.service;

import com.perfectrum.backend.domain.entity.ReviewEntity;
import com.perfectrum.backend.domain.entity.UserEntity;
import com.perfectrum.backend.domain.repository.ReviewRepository;
import com.perfectrum.backend.domain.repository.UserRepository;
import com.perfectrum.backend.dto.review.MyReviewDto;
import com.perfectrum.backend.dto.user.UserInfoDto;
import com.perfectrum.backend.mapper.UserInfoMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class UserServiceTest {

    private final UserRepository userRepository;
    private final UserInfoMapper userInfoMapper;
    private final ReviewRepository reviewRepository;

    @Autowired
    UserServiceTest(UserRepository userRepository, UserInfoMapper userInfoMapper, ReviewRepository reviewRepository){
        this.userRepository = userRepository;
        this.userInfoMapper = userInfoMapper;
        this.reviewRepository = reviewRepository;
    }

    @Test
    public void 내_정보_조회_테스트(){
        // given
        String testId = "kakao123145";

        // when
        UserEntity userEntity = userRepository.findByUserId(testId).get();
        if(userEntity != null){
            UserInfoDto userInfo = userInfoMapper.toDto(userEntity);
            // then
            System.out.println(userInfo.toString());
        }else{
            System.out.println("fail");
        }
    }

    @Test
    public void 회원_가입_추가_정보(){
        // given
        String testId = "kakao123456";
        String gender = "Men";
        String seasons = "winter";
        Integer accordClass = 3;

        // when
        Optional<UserEntity> optionalUser = userRepository.findByUserId(testId);
        if(optionalUser.isPresent()){
            UserEntity user = optionalUser.get();
            user.setGender(gender);
            user.setSeasons(seasons);
            user.setAccordClass(accordClass);

            userRepository.save(user); // 저장
        }else{
            System.out.println("fail");
        }
    }

    @Test
    public void 닉네임_중복_체크(){
        // given
        String nickname = "진진자라";

        // when
        Optional<UserEntity> optionalUser = userRepository.findByNickname(nickname);
        if(optionalUser.isPresent()){
            System.out.println("fail");
        }else{
            System.out.println("okay");
        }
    }
    @Test
    public void 내_정보_수정_테스트(){
        // given
        Integer idx = 4;
        String testId = "kakao123456";
        String nickname = "닉네임이지롱";
        String profileImg = null;
        String gender = "Men";
        String seasons = "summer";
        Integer accordClass = 1;

        UserInfoDto userInfoDto = UserInfoDto.builder()
                .idx(idx)
                .userId(testId)
                .nickname(nickname)
                .profileImg(profileImg)
                .gender(gender)
                .seasons(seasons)
                .accordClass(accordClass)
                .build();

        // when
        Optional<UserEntity> optionalUser = userRepository.findByUserId(testId);
        if(optionalUser.isPresent()){
            UserEntity userEntity = optionalUser.get();
            userInfoMapper.updateFromDto(userInfoDto, userEntity);
            userRepository.save(userEntity);

            // then
            userInfoDto = userInfoMapper.toDto(userEntity);
            System.out.println(userInfoDto.toString());
            System.out.println("okay");
        }else{
            System.out.println("fail");
        }
    }

    @Disabled
    @Test
    public void 회원_탈퇴(){
        String testId = "kakao123456";

        Optional<UserEntity> optionalUser = userRepository.findByUserId(testId);
        if(optionalUser.isPresent()){
            UserEntity userEntity = optionalUser.get();
            userRepository.delete(userEntity);
            System.out.println("okay");
        }else{
            System.out.println("fail");
        }
    }

    @Test
    public void 작성한_리뷰_조회_테스트(){
        // given
        String testId = "kakao2440303858";
        String type = "평점순"; // 최신순, 평점순

        Integer lastIdx = null; // 마지막으로 본 리뷰의 idx(null이면 처음부터 조회 -> DB에서 가장 최신 게시글 번호 찾기)
        Integer lastScore = null; // 마지막으로 본 리뷰의 평점, 최신순으로만 조회한다면 lastScore 필요없음

        Integer pageSize = 8; // 한 페이지에 조회할 게시글 수
        Pageable pageable = PageRequest.ofSize(pageSize);

        // when
        Optional<UserEntity> optionalUser = userRepository.findByUserId(testId);

        if(optionalUser.isPresent()){
            UserEntity userEntity = optionalUser.get();

            Slice<ReviewEntity> reviews = reviewRepository.findByUserAndIsDelete(userEntity, false);

            if(!reviews.isEmpty()){
                if(lastIdx == null){ // null인 경우 가장 최신 글 idx 찾아서 넣어줌
                    lastIdx = reviewRepository.findTop1ByUserOrderByIdxDesc(userEntity).getIdx() + 1; // 최신 게시물을 포함해야 하므로 +1
                }
                if(lastScore == null){
                    lastScore = reviewRepository.findTop1ByUserOrderByTotalScoreDescIdxDesc(userEntity).getTotalScore() + 1;
                }

                if (type.equals("최신순")) {
                    reviews = reviewRepository.findByUserAndIsDeleteOrderByIdxDesc(userEntity, false,  lastIdx, pageable);
                }else{ // 평점순
                    reviews = reviewRepository.findByUseAndIsDeleterOrderByTotalScoreDescIdxDesc(userEntity, false, lastScore, lastIdx, pageable);
                }

                boolean hasNext = reviews.hasNext(); // 다음 결과 있는지 없는지 여부(false면 마지막 페이지)
                List<MyReviewDto> myReviewList = new ArrayList<>();
                for(ReviewEntity r : reviews){
                    MyReviewDto myReviewDto = MyReviewDto.builder()
                            .idx(r.getIdx())
                            .perfumeIdx(r.getPerfume().getIdx())
                            .perfumeName(r.getPerfume().getPerfumeName())
                            .reviewImg(r.getReviewImg())
                            .totalScore(r.getTotalScore())
                            .longevity(r.getLongevity())
                            .sillageScore(r.getSillageScore())
                            .content(r.getContent())
                            .time(r.getTime())
                            .updateTime(r.getUpdateTime())
                            .build();

                    myReviewList.add(myReviewDto);
                }
                for(MyReviewDto d : myReviewList){
                    System.out.println(d.toString());
                }
                System.out.println("hasNext : " + hasNext);
            }else{
                System.out.println("리뷰 없음");
            }
        }
    }

    @Test
    public void 리뷰_전체_개수(){
        String testId = "kakao123145";

        Optional<UserEntity> optionalUserEntity = userRepository.findByUserId(testId);
        if(optionalUserEntity.isPresent()){
            UserEntity userEntity = optionalUserEntity.get();
            Integer count = reviewRepository.countByUserAndIsDelete(userEntity, false);

            System.out.println("======== 리뷰 개수 ========");
            System.out.println(count);
        }
    }

    @Test
    public void 리뷰_전체_평점() {
        String testId = "kakao2435577184";

        Optional<UserEntity> optionalUserEntity = userRepository.findByUserId(testId);
        if(optionalUserEntity.isPresent()){
            UserEntity userEntity = optionalUserEntity.get();
            Slice<ReviewEntity> reviewEntities = reviewRepository.findByUserAndIsDelete(userEntity, false);
            double avg_score = 0;
            if(!reviewEntities.isEmpty()){
                int count = reviewRepository.countByUserAndIsDelete(userEntity, false);
                double total_score = reviewRepository.sumByUserAndIsDelete(userEntity, false);

                avg_score = total_score/count;
            }

            System.out.println(avg_score);
        }
    }
}
