package com.perfectrum.backend.dto.review;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ReviewViewDto {
    private Integer idx;

    private String userNickname;
    private String userProfileimg;

    private String perfumeName;

    private String reviewImg;

    private Integer totalScore;
    private Integer longevity;
    private Integer sillageScore;
    private String content;
    private Integer likeCount;

    private LocalDateTime time;
    private LocalDateTime updateTime;
    private boolean isDelete;
}
