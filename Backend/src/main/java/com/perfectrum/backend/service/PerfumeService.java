package com.perfectrum.backend.service;

import com.perfectrum.backend.dto.perfume.PerfumeViewDto;

import java.util.List;

public interface PerfumeService {
    List<PerfumeViewDto> viewBestPerfume(String decodeId);

    List<PerfumeViewDto> viewTodayPerfume(String decodeId);
}
