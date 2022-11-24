package com.perfectrum.backend.service;

import com.perfectrum.backend.domain.entity.*;
import com.perfectrum.backend.domain.repository.*;
import com.perfectrum.backend.dto.MyPage.HaveListDto;
import com.perfectrum.backend.dto.MyPage.UserAccordClassDto;
import com.perfectrum.backend.dto.MyPage.WishListDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class MyPageServiceTest {

    private UserRepository userRepository;
    private WishListRepository wishListRepository;
    private HaveListRepository haveListRepository;
    private PerfumeRepository perfumeRepository;
    private UserAccordClassRepository userAccordClassRepository;
    private AccordClassRepository accordClassRepository;

    @Autowired
    MyPageServiceTest(UserRepository userRepository, WishListRepository wishListRepository, HaveListRepository haveListRepository,
                      PerfumeRepository perfumeRepository, UserAccordClassRepository userAccordClassRepository,
                      AccordClassRepository accordClassRepository){
        this.userRepository = userRepository;
        this.wishListRepository = wishListRepository;
        this.haveListRepository = haveListRepository;
        this.perfumeRepository = perfumeRepository;
        this.userAccordClassRepository = userAccordClassRepository;
        this.accordClassRepository = accordClassRepository;
    }

    @Test
    public void 선호향_조회_테스트(){
        String testId = "kakao2435577184";

        Optional<UserEntity> userEntity = userRepository.findByUserId(testId);
        List<UserAccordClassDto> list = new ArrayList<>();
        if(userEntity.isPresent()){
            UserEntity user = userEntity.get();

            List<UserAccordClassEntity> userAccordClassEntities = userAccordClassRepository.findByUser(user);
            if(!userAccordClassEntities.isEmpty()){
                for(UserAccordClassEntity u : userAccordClassEntities){
                    UserAccordClassDto dto = UserAccordClassDto.builder()
                            .accordClassIdx(u.getAccordClass().getIdx())
                            .accordClassName(u.getAccordClass().getClassName())
                            .accordClassCount(u.getAccordClassCount())
                            .build();

                    list.add(dto);
                }
            }
        }

        for(UserAccordClassDto u : list){
            System.out.println(u.toString());
        }
    }

    @Test
    public void wishList_조회() {
        // given
        String testId = "kakao2435577184";


        // when
        Optional<UserEntity> optionalUserEntity = userRepository.findByUserId(testId);
        if(optionalUserEntity.isPresent()){
            UserEntity userEntity = optionalUserEntity.get();
            List<WishListEntity> list = wishListRepository.findByUserAndIsDelete(userEntity, false);
            List<WishListDto> dtoList = new ArrayList<>();
            if(!list.isEmpty()){
                for(WishListEntity w : list){
                    WishListDto dto = WishListDto.builder()
                            .perfumeIdx(w.getPerfume().getIdx())
                            .perfumeName(w.getPerfume().getPerfumeName())
                            .braneName(w.getPerfume().getBrandName())
                            .perfumeImg(w.getPerfume().getPerfumeImg())
                            .isDelete(w.getIsDelete())
                            .build();

                    dtoList.add(dto);
                }

            }else{
                dtoList = null;
            }

            // then
            if(dtoList != null){
                for(WishListDto w : dtoList) {
                    System.out.println(w.toString());
                }
            }else{
                System.out.println("위시 향수 없음");
            }
        }
    }

    @Disabled
    @Test
    public void wish에서_have로_이동(){
        // given
        String testId = "kakao2435577184";
        Integer idx = 1061; // 위시리스트 idx

        // when
        Optional<UserEntity> optionalUserEntity = userRepository.findByUserId(testId);
        if(optionalUserEntity.isPresent()){
            UserEntity userEntity = optionalUserEntity.get();
            // 1. 보유리스트에 추가로 넣기
            WishListEntity wishListEntity = wishListRepository.findByIdx(idx);
            PerfumeEntity perfumeEntity = perfumeRepository.findByIdx(wishListEntity.getPerfume().getIdx());
            HaveListEntity haveListEntity = HaveListEntity.builder()
                    .user(userEntity)
                    .perfume(perfumeEntity)
                    .build();
            haveListRepository.save(haveListEntity);

            // 2. 위시리스트에 있는 향수 지우기
            WishListEntity updateWishList = WishListEntity.builder().idx(idx).perfume(wishListEntity.getPerfume()).user(wishListEntity.getUser()).isDelete(true).build();
            wishListRepository.save(updateWishList);
      }
    }

    @Test
    public void wishList_삭제(){
        String testId = "kakao123145";
        Integer idx = 1077;

        Optional<UserEntity> userEntityOptional = userRepository.findByUserId(testId);
        Optional<WishListEntity> wishListEntity = wishListRepository.findByUserAndIdx(userEntityOptional,idx);

        if(wishListEntity.isPresent()){
            UserEntity user = userEntityOptional.get();
            WishListEntity wishList = wishListEntity.get();
            WishListEntity updateWishList = WishListEntity.builder().idx(idx).perfume(wishList.getPerfume()).user(wishList.getUser()).isDelete(true).build();
            wishListRepository.save(updateWishList);

            // userAccordClass에도 삭제(cnt-1)
            PerfumeEntity perfume = wishList.getPerfume();
            List<AccordClassEntity> accordClassEntities = accordClassRepository.findByPerfumeAccordClass(perfume);
            for(AccordClassEntity a : accordClassEntities){
                Optional<UserAccordClassEntity> userAccordClass = userAccordClassRepository.findByUserAndAccordClass(user, a);
                if(userAccordClass.isPresent()){
                    UserAccordClassEntity updateUserAccordClass = UserAccordClassEntity.builder()
                            .idx(userAccordClass.get().getIdx())
                            .user(userAccordClass.get().getUser())
                            .accordClass(userAccordClass.get().getAccordClass())
                            .accordClassCount(userAccordClass.get().getAccordClassCount()-1)
                            .build();

                    userAccordClassRepository.save(updateUserAccordClass);
                }
            }
        }
    }

    @Test
    public void haveList_삭제(){
        String testId = "kakao123145";
        Integer idx = 243;

        Optional<UserEntity> userEntityOptional = userRepository.findByUserId(testId);
        Optional<HaveListEntity> haveListEntity = haveListRepository.findByUserAndIdx(userEntityOptional,idx);

        if(haveListEntity.isPresent()){
            UserEntity user = userEntityOptional.get();
            HaveListEntity haveList = haveListEntity.get();
            HaveListEntity updateHaveList = HaveListEntity.builder().idx(idx).perfume(haveList.getPerfume()).user(haveList.getUser()).isDelete(true).build();
            haveListRepository.save(updateHaveList);

            // userAccordClass에도 삭제(cnt-1)
            PerfumeEntity perfume = haveList.getPerfume();
            List<AccordClassEntity> accordClassEntities = accordClassRepository.findByPerfumeAccordClass(perfume);
            for(AccordClassEntity a : accordClassEntities){
                Optional<UserAccordClassEntity> userAccordClass = userAccordClassRepository.findByUserAndAccordClass(user, a);
                if(userAccordClass.isPresent()){
                    UserAccordClassEntity updateUserAccordClass = UserAccordClassEntity.builder()
                            .idx(userAccordClass.get().getIdx())
                            .user(userAccordClass.get().getUser())
                            .accordClass(userAccordClass.get().getAccordClass())
                            .accordClassCount(userAccordClass.get().getAccordClassCount()-1)
                            .build();

                    userAccordClassRepository.save(updateUserAccordClass);
                }
            }
        }
    }

    @Test
    public void haveList_조회(){
        String testId = "kakao2435577184";

        Optional<UserEntity> optionalUserEntity = userRepository.findByUserId(testId);
        if(optionalUserEntity.isPresent()){
            UserEntity userEntity = optionalUserEntity.get();
            List<HaveListEntity> list = haveListRepository.findByUserAndIsDelete(userEntity, false);
            List<HaveListDto> dtoList = new ArrayList<>();

            if(!list.isEmpty()){
                for(HaveListEntity h : list){
                    HaveListDto dto = HaveListDto.builder()
                            .perfumeIdx(h.getPerfume().getIdx())
                            .perfumeName(h.getPerfume().getPerfumeName())
                            .braneName(h.getPerfume().getBrandName())
                            .perfumeImg(h.getPerfume().getPerfumeImg())
                            .isDelete(h.getIsDelete())
                            .build();

                    dtoList.add(dto);
                }
            }else{
                dtoList = null;
            }

            if(dtoList != null){
                for(HaveListDto h : dtoList){
                    System.out.println(h.toString());
                }
            }else{
                System.out.println("보유한 향수 없음");
            }
        }
    }


}
