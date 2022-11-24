package com.perfectrum.backend.dto.review;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRegistDto {
    private String reviewImg;
    private Integer totalScore;
    private Integer longevity;
    private Integer sillageScore;
    private String content;
}