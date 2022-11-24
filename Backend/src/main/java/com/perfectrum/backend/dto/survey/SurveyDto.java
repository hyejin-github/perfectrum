package com.perfectrum.backend.dto.survey;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SurveyDto {

    String gender;
    String season;
    Integer longevity;
    String accordClass;
}
