package com.perfectrum.backend.dto.perfume;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PerfumeViewDto {
    private Integer idx;
    private String brandName;
    private String perfumeName;
    private String concentration;
    private String gender;
    private String scent;
    private String topNotes;
    private String middleNotes;
    private String baseNotes;
    private Float itemRating;
    private String perfumeImg;
    private String description;
    private String seasons;
    private String timezone;
    private Integer longevity;
    private Integer sillage;
    private Integer wishCount;
    private Integer haveCount;
}
