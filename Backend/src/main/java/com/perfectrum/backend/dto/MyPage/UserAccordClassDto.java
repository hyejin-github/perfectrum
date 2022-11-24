package com.perfectrum.backend.dto.MyPage;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserAccordClassDto {
    private Integer accordClassIdx;
    private String accordClassName;
    private Integer accordClassCount;
}
