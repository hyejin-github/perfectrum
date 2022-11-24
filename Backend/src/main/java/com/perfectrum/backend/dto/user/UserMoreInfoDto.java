package com.perfectrum.backend.dto.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserMoreInfoDto {
    private String gender;
    private String seasons;
    private Integer accordClass;
}
