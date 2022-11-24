package com.perfectrum.backend.service.impl;

import com.perfectrum.backend.domain.entity.*;
import com.perfectrum.backend.domain.repository.AccordClassRepository;
import com.perfectrum.backend.domain.repository.HaveListRepository;
import com.perfectrum.backend.domain.repository.UserAccordClassRepository;
import com.perfectrum.backend.domain.repository.UserRepository;
import com.perfectrum.backend.dto.MyPage.HaveListDto;
import com.perfectrum.backend.service.HaveListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HaveListServiceImpl implements HaveListService {

    private UserRepository userRepository;
    private HaveListRepository haveListRepository;
    private AccordClassRepository accordClassRepository;
    private UserAccordClassRepository userAccordClassRepository;

    @Autowired
    HaveListServiceImpl(UserRepository userRepository, HaveListRepository haveListRepository, AccordClassRepository accordClassRepository,
                        UserAccordClassRepository userAccordClassRepository) {
        this.userRepository = userRepository;
        this.haveListRepository = haveListRepository;
        this.accordClassRepository = accordClassRepository;
        this.userAccordClassRepository = userAccordClassRepository;
    }

    @Override
    public List<HaveListDto> viewHaveList(String decodeId) {
        List<HaveListDto> haveList = new ArrayList<>();
        Optional<UserEntity> userEntity = userRepository.findByUserId(decodeId);

        if(userEntity.isPresent()){
            UserEntity user = userEntity.get();
            List<HaveListEntity> haveListEntityList = haveListRepository.findByUserAndIsDelete(user, false);

            if(!haveListEntityList.isEmpty()){
                for(HaveListEntity h : haveListEntityList){
                    HaveListDto haveListDto = HaveListDto.builder()
                            .idx(h.getIdx())
                            .perfumeIdx(h.getPerfume().getIdx())
                            .perfumeName(h.getPerfume().getPerfumeName())
                            .braneName(h.getPerfume().getBrandName())
                            .perfumeImg(h.getPerfume().getPerfumeImg())
                            .isDelete(h.getIsDelete())
                            .build();

                    haveList.add(haveListDto);
                }
            }else{
                haveList = null;
            }
        }
        return haveList;
    }

    @Override
    public void deleteHaveList(String decodeId, Integer idx) {
        Optional<UserEntity> userEntity = userRepository.findByUserId(decodeId);
        Optional<HaveListEntity> haveListEntity = haveListRepository.findByUserAndIdx(userEntity,idx);

        if(haveListEntity.isPresent()){
            UserEntity user = userEntity.get();
            HaveListEntity haveList = haveListEntity.get();
            HaveListEntity updateHaveList = HaveListEntity.builder().idx(idx).perfume(haveList.getPerfume()).user(haveList.getUser()).isDelete(true).build();
            haveListRepository.save(updateHaveList);

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
}
