package com.perfectrum.backend.dto.perfume;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfumeAccordsDto {

    String accordName;
    String accordDescription;
    String accordImg;
    Integer accordClass;
}
