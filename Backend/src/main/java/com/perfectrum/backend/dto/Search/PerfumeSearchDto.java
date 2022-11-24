package com.perfectrum.backend.dto.Search;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfumeSearchDto {

    List<String> gender;
    List<String> duration;
    List<String> accordClass;

    Integer lastIdx;
    Integer pageSize;

}
