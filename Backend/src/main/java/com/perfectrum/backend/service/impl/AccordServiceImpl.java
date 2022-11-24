package com.perfectrum.backend.service.impl;

import com.perfectrum.backend.domain.entity.AccordClassEntity;
import com.perfectrum.backend.domain.entity.AccordEntity;
import com.perfectrum.backend.domain.repository.AccordClassRepository;
import com.perfectrum.backend.domain.repository.AccordRepository;
import com.perfectrum.backend.dto.perfume.AccordMoreInfoDto;
import com.perfectrum.backend.service.AccordService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccordServiceImpl implements AccordService {
    private AccordRepository accordRepository;
    private AccordClassRepository accordClassRepository;

    AccordServiceImpl(AccordRepository accordRepository, AccordClassRepository accordClassRepository){
        this.accordRepository = accordRepository;
        this.accordClassRepository = accordClassRepository;
    }
    @Override
    public List<AccordMoreInfoDto> viewAccordDocs(Integer idx) {
        AccordClassEntity accordClassEntity = accordClassRepository.findByIdx(idx);
        List<AccordEntity> accordEntityList = accordRepository.findByAccordClass(accordClassEntity);
        if(!accordEntityList.isEmpty()){
            List<AccordMoreInfoDto> list = new ArrayList<>();
            for(AccordEntity ae : accordEntityList){
                AccordMoreInfoDto accordList = AccordMoreInfoDto.builder()
                        .accordName(ae.getAccordName())
                        .accordImg(ae.getAccordImg())
                        .accordDescription(ae.getAccordDescription())
                        .build();

                list.add(accordList);
            }

            return list;
        }
        return null;
    }
}
