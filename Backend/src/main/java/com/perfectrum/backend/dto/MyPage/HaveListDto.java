package com.perfectrum.backend.dto.MyPage;

import com.perfectrum.backend.domain.entity.UserEntity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HaveListDto {
    Integer idx;
    Integer perfumeIdx;
    String perfumeName;
    String braneName;
    String perfumeImg;

    Boolean isDelete;
}
