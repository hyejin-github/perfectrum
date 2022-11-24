package com.perfectrum.backend.service.impl;

import com.perfectrum.backend.domain.entity.UserAccordClassEntity;
import com.perfectrum.backend.domain.entity.UserEntity;
import com.perfectrum.backend.domain.repository.UserAccordClassRepository;
import com.perfectrum.backend.domain.repository.UserRepository;
import com.perfectrum.backend.dto.MyPage.UserAccordClassDto;
import com.perfectrum.backend.service.UserAccordClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserAccordClassServiceImpl implements UserAccordClassService {
    private UserRepository userRepository;
    private UserAccordClassRepository userAccordClassRepository;

    @Autowired
    UserAccordClassServiceImpl(UserRepository userRepository, UserAccordClassRepository userAccordClassRepository){
        this.userRepository = userRepository;
        this.userAccordClassRepository = userAccordClassRepository;
    }
    @Override
    public List<UserAccordClassDto> viewPieChart(String decodeId) {
        Optional<UserEntity> userEntity = userRepository.findByUserId(decodeId);
        List<UserAccordClassDto> list = new ArrayList<>();
        if(userEntity.isPresent()){
            UserEntity user = userEntity.get();

            List<UserAccordClassEntity> userAccordClassEntities = userAccordClassRepository.findByUser(user);
            if(!userAccordClassEntities.isEmpty()){
                for(UserAccordClassEntity u : userAccordClassEntities){
                    if(u.getAccordClassCount() != 0){
                        UserAccordClassDto dto = UserAccordClassDto.builder()
                                .accordClassIdx(u.getAccordClass().getIdx())
                                .accordClassName(u.getAccordClass().getClassName())
                                .accordClassCount(u.getAccordClassCount())
                                .build();

                        list.add(dto);
                    }
                }
            }else{
                list = null;
            }
        }
        return list;
    }
}
