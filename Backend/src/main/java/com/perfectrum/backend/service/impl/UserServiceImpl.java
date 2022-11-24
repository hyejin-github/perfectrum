package com.perfectrum.backend.service.impl;

import com.perfectrum.backend.domain.entity.ReviewEntity;
import com.perfectrum.backend.domain.entity.UserEntity;
import com.perfectrum.backend.domain.repository.ReviewRepository;
import com.perfectrum.backend.domain.repository.UserRepository;
import com.perfectrum.backend.dto.review.MyReviewDto;
import com.perfectrum.backend.dto.review.MyReviewListDto;
import com.perfectrum.backend.dto.user.UserInfoDto;
import com.perfectrum.backend.dto.user.UserMoreInfoDto;
import com.perfectrum.backend.dto.user.UserUpdateInfoDto;
import com.perfectrum.backend.mapper.UserInfoMapper;
import com.perfectrum.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserInfoMapper userInfoMapper;
    private final ReviewRepository reviewRepository;

    @Autowired
    UserServiceImpl(UserRepository userRepository, UserInfoMapper userInfoMapper, ReviewRepository reviewRepository){
        this.userRepository = userRepository;
        this.userInfoMapper = userInfoMapper;
        this.reviewRepository = reviewRepository;
    }
    @Override
    public UserInfoDto getUserInfo(String decodeId) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUserId(decodeId);
        if(userEntityOptional.isPresent()){
            UserEntity userEntity = userEntityOptional.get();

            return userInfoMapper.toDto(userEntity);
        }
        return null;
    }

    @Override
    public void addMoreUserInfo(String decodeId, UserMoreInfoDto userMoreInfoDto) {
        String gender = userMoreInfoDto.getGender();
        String seasons = userMoreInfoDto.getSeasons();
        Integer accorClass = userMoreInfoDto.getAccordClass();

        Optional<UserEntity> userEntityOptional = userRepository.findByUserId(decodeId);
        if(userEntityOptional.isPresent()){
            UserEntity user = userEntityOptional.get();
            user.setGender(gender);
            user.setSeasons(seasons);
            user.setAccordClass(accorClass);

            userRepository.save(user);
        }
    }

    @Override
    public UserInfoDto updateUserInfo(String decodeId, UserUpdateInfoDto userUpdateInfoDto) {
        String nickname = userUpdateInfoDto.getNickname();
        String profileImg = userUpdateInfoDto.getProfileImg();
        String gender = userUpdateInfoDto.getGender();
        String seasons = userUpdateInfoDto.getSeasons();
        Integer accordClass = userUpdateInfoDto.getAccordClass();

        Optional<UserEntity> userEntity = userRepository.findByUserId(decodeId);
        if(userEntity.isPresent()){
            UserEntity user = userEntity.get();
            UserInfoDto userInfoDto = UserInfoDto.builder()
                    .idx(user.getIdx())
                    .userId(user.getUserId())
                    .profileImg(profileImg)
                    .nickname(nickname)
                    .gender(gender)
                    .seasons(seasons)
                    .accordClass(accordClass)
                    .build();

            userInfoMapper.updateFromDto(userInfoDto, user);
            userRepository.save(user);

            return userInfoDto;
        }
        return null;
    }

    @Override
    public boolean checkNickName(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }

    @Override
    public void deleteUser(String decodeId) {
        UserEntity user = userRepository.findByUserId(decodeId).get();
        userRepository.delete(user);
    }

    @Override
    public Map<String,Object> viewMyReviews(String nickname, MyReviewListDto myReviewListDto) {
        Map<String, Object> data = new HashMap<>();

        String type = myReviewListDto.getType();
        Integer lastIdx = myReviewListDto.getLastIdx();
        Integer lastScore = myReviewListDto.getLastScore();
        Integer pageSize = myReviewListDto.getPageSize();

        Pageable pageable = Pageable.ofSize(pageSize);

        Optional<UserEntity> userEntityOptional = userRepository.findByNickname(nickname);
        if(userEntityOptional.isPresent()){
            UserEntity userEntity = userEntityOptional.get();
            Slice<ReviewEntity> reviews = reviewRepository.findByUserAndIsDelete(userEntity, false);

            if(!reviews.isEmpty()){
                if(type == null){
                    type = "평점순";
                }
                if(lastIdx == null){
                    lastIdx = reviewRepository.findTop1ByUserOrderByIdxDesc(userEntity).getIdx() + 1; // 최신 게시물을 포함해야 하므로 +1
                }
                if(lastScore == null){
                    lastScore = reviewRepository.findTop1ByUserOrderByTotalScoreDescIdxDesc(userEntity).getTotalScore() + 1;
                }

                if (type.equals("최신순")) {
                    reviews = reviewRepository.findByUserAndIsDeleteOrderByIdxDesc(userEntity, false, lastIdx, pageable);
                }else{ // 평점순
                    reviews = reviewRepository.findByUseAndIsDeleterOrderByTotalScoreDescIdxDesc(userEntity, false, lastScore, lastIdx, pageable);
                }

                boolean hasNext = reviews.hasNext();
                data.put("hasNext", hasNext);

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
                            .isDelete(r.getIsDelete())
                            .updateTime(r.getUpdateTime())
                            .build();

                    myReviewList.add(myReviewDto);
                }
                data.put("myReviewList",myReviewList);
            }else{
                data.put("myReviewList", null);
                data.put("hasNext", null);
            }
        }

        return data;
    }

    @Override
    public Object getTotalReviews(String nickname) {
        Optional<UserEntity> userEntityOptional = userRepository.findByNickname(nickname);
        Integer count = 0;

        if(userEntityOptional.isPresent()){
            UserEntity userEntity = userEntityOptional.get();
            count = reviewRepository.countByUserAndIsDelete(userEntity, false);
        }
        return count;
    }

    @Override
    public Object getAvgReviews(String nickname) {
        Optional<UserEntity> userEntityOptional = userRepository.findByNickname(nickname);
        double avg_score = 0;
        if(userEntityOptional.isPresent()){
            UserEntity userEntity = userEntityOptional.get();
            Slice<ReviewEntity> reviewEntities = reviewRepository.findByUser(userEntity);
            if(!reviewEntities.isEmpty()){
                int count = reviewRepository.countByUserAndIsDelete(userEntity, false);
                double total_score = reviewRepository.sumByUserAndIsDelete(userEntity, false);

                avg_score = total_score/count;
            }
        }
        return avg_score;
    }

    @Override
    public String getUserId(String nickname) {
        return userRepository.findByNickname(nickname).get().getUserId();
    }
}