package com.perfectrum.backend.service.impl;

import com.perfectrum.backend.domain.entity.AccordClassEntity;
import com.perfectrum.backend.domain.entity.UserEntity;
import com.perfectrum.backend.domain.repository.AccordClassRepository;
import com.perfectrum.backend.domain.repository.UserRepository;
import com.perfectrum.backend.dto.MyPage.ViewAccordClassDto;
import com.perfectrum.backend.service.AccordClassService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccordClassServiceImpl implements AccordClassService {
    private AccordClassRepository accordClassRepository;

    AccordClassServiceImpl(AccordClassRepository accordClassRepository){
        this.accordClassRepository = accordClassRepository;
    }
    @Override
    public ViewAccordClassDto viewAccordClass(Integer idx) {
        AccordClassEntity accordClassEntity = accordClassRepository.findByIdx(idx);
        ViewAccordClassDto dto = ViewAccordClassDto.builder()
                .className(accordClassEntity.getClassName())
                .classDescription(accordClassEntity.getClassDescription())
                .build();
        return dto;
    }
}
