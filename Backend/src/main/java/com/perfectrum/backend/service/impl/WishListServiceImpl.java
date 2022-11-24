package com.perfectrum.backend.service.impl;

import com.perfectrum.backend.domain.entity.*;
import com.perfectrum.backend.domain.repository.*;
import com.perfectrum.backend.dto.MyPage.WishListDto;
import com.perfectrum.backend.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WishListServiceImpl implements WishListService {
    private UserRepository userRepository;
    private WishListRepository wishListRepository;
    private HaveListRepository haveListRepository;
    private PerfumeRepository perfumeRepository;
    private AccordClassRepository accordClassRepository;
    private UserAccordClassRepository userAccordClassRepository;

    @Autowired
    WishListServiceImpl(UserRepository userRepository, WishListRepository wishListRepository, AccordClassRepository accordClassRepository,
                        HaveListRepository haveListRepository, PerfumeRepository perfumeRepository, UserAccordClassRepository userAccordClassRepository){
        this.userRepository = userRepository;
        this.wishListRepository = wishListRepository;
        this.haveListRepository = haveListRepository;
        this.perfumeRepository = perfumeRepository;
        this.accordClassRepository = accordClassRepository;
        this.userAccordClassRepository = userAccordClassRepository;
    }
    @Override
    public List<WishListDto> viewWishList(String decodeId) {
        List<WishListDto> wishList = new ArrayList<>();
        Optional<UserEntity> userEntity = userRepository.findByUserId(decodeId);

        if(userEntity.isPresent()){
            UserEntity user = userEntity.get();
            List<WishListEntity> wishListEntityList = wishListRepository.findByUserAndIsDelete(user, false);

            if(!wishListEntityList.isEmpty()){
                for(WishListEntity w : wishListEntityList){
                    WishListDto wishListDto = WishListDto.builder()
                            .idx(w.getIdx())
                            .perfumeIdx(w.getPerfume().getIdx())
                            .perfumeName(w.getPerfume().getPerfumeName())
                            .braneName(w.getPerfume().getBrandName())
                            .perfumeImg(w.getPerfume().getPerfumeImg())
                            .isDelete(w.getIsDelete())
                            .build();

                    wishList.add(wishListDto);
                }
            }else{
                wishList = null;
            }
        }
        return wishList;
    }

    @Override
    public void moveWishToHave(String decodeId, Integer idx) {
        Optional<UserEntity> userEntity = userRepository.findByUserId(decodeId);
        if(userEntity.isPresent()){
            UserEntity user = userEntity.get();
            WishListEntity wishListEntity = wishListRepository.findByIdx(idx);
            PerfumeEntity perfumeEntity = perfumeRepository.findByIdx(wishListEntity.getPerfume().getIdx());
            HaveListEntity haveListEntity = HaveListEntity.builder()
                    .user(user)
                    .perfume(perfumeEntity)
                    .build();
            haveListRepository.save(haveListEntity);

            WishListEntity updateWishList = WishListEntity.builder().idx(idx).perfume(wishListEntity.getPerfume()).user(wishListEntity.getUser()).isDelete(true).build();
            wishListRepository.save(updateWishList);
        }
    }

    @Override
    public void deleteWishList(String decodeId, Integer idx) {
        Optional<UserEntity> userEntity = userRepository.findByUserId(decodeId);
        Optional<WishListEntity> wishListEntity = wishListRepository.findByUserAndIdx(userEntity,idx);

        if(wishListEntity.isPresent()){
            UserEntity user = userEntity.get();
            WishListEntity wishList = wishListEntity.get();
            WishListEntity updateWishList = WishListEntity.builder().idx(idx).perfume(wishList.getPerfume()).user(wishList.getUser()).isDelete(true).build();
            wishListRepository.save(updateWishList);

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
}
