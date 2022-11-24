package com.perfectrum.backend.dto.review;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MyReviewDto {
    private Integer idx;

    private Integer perfumeIdx;
    private String perfumeName;

    private String reviewImg;
    private Integer totalScore;
    private Integer longevity;
    private Integer sillageScore;
    private String content;
    private boolean isDelete;

    private LocalDateTime time;
    private LocalDateTime updateTime;
}
