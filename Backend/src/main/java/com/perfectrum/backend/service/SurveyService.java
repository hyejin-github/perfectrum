package com.perfectrum.backend.service;

import com.perfectrum.backend.domain.entity.PerfumeEntity;
import com.perfectrum.backend.dto.survey.SurveyDto;

import java.util.Map;

public interface SurveyService {

    Map<String,Object> surveyResult(String decodeId, SurveyDto surveyDto);

}
