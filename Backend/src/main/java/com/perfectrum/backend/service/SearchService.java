package com.perfectrum.backend.service;


import com.perfectrum.backend.dto.Search.PerfumeSearchDto;

import java.util.Map;

public interface SearchService {


    Map<String, Object> searchPerfume(String decodeId, PerfumeSearchDto perfumeSearchDto);
}
