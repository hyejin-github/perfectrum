package com.perfectrum.backend.service;

import com.perfectrum.backend.dto.MyPage.UserAccordClassDto;

import java.util.List;

public interface UserAccordClassService {
    List<UserAccordClassDto> viewPieChart(String decodeId);
}
