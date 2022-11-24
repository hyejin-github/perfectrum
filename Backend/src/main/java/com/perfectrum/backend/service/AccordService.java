package com.perfectrum.backend.service;

import com.perfectrum.backend.dto.perfume.AccordMoreInfoDto;

import java.util.List;

public interface AccordService {
    List<AccordMoreInfoDto> viewAccordDocs(Integer idx);
}
