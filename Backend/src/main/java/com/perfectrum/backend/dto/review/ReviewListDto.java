package com.perfectrum.backend.dto.review;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewListDto {
        private String type;
        private Integer lastIdx;
        private Integer lastTotalScore;
        private Integer lastLikeCount;
        private Integer pageSize;

}
