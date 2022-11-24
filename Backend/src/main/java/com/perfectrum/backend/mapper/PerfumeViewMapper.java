package com.perfectrum.backend.mapper;

import com.perfectrum.backend.domain.entity.PerfumeEntity;
import com.perfectrum.backend.dto.perfume.PerfumeViewDto;
import org.mapstruct.Mapper;

@Mapper(componentModel =  "spring")
public interface PerfumeViewMapper extends GenericMapper<PerfumeViewDto, PerfumeEntity> {

    @Override
    PerfumeViewDto toDto(PerfumeEntity perfumeEntity);

    @Override
    PerfumeEntity toEntity(PerfumeViewDto perfumeViewDto);
}
