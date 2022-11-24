package com.perfectrum.backend.dto.perfume;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccordBasicInfoDto {

    Integer accordIdx;
    String accordName;
}
