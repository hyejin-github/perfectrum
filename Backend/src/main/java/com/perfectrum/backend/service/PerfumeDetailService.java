package com.perfectrum.backend.service;

import com.perfectrum.backend.domain.entity.PerfumeEntity;
import com.perfectrum.backend.dto.review.ReviewListDto;
import com.perfectrum.backend.dto.review.ReviewRegistDto;
import com.perfectrum.backend.dto.review.ReviewViewDto;

import java.util.Map;

public interface PerfumeDetailService {

    Map<String,Object> getPerfumeDetail(String decodeId, Integer perfumeIdx, ReviewListDto reviewListDto);

    Map<String,Object> getAcoordMoreInfo(Integer accordIdx);
    Map<String,Object> addWishList(String decodeId, Integer perfumeIdx);

    Map<String,Object> addHaveList(String decodeId,Integer perfumeIdx);

    void registReview(String decodeId,Integer perfumeIdx, ReviewRegistDto reviewRegistDto);

    void updateReview(String decodeId,Integer perfumeIdx, Integer reviewIdx, ReviewRegistDto reviewRegistDto);

    void deleteReview(String decodeId, Integer perfumeIdx, Integer reviewIdx);

    void clickLike(String decodeId, Integer perfumeIdx, Integer reviewIdx );

    void unclickLike(String decodeId, Integer perfumeIdx, Integer reviewIdx );
}

