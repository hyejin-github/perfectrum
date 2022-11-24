package com.perfectrum.backend.dto.review;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MyReviewListDto {
    private String type;
    private Integer lastIdx;
    private Integer lastScore;
    private Integer pageSize;
}
