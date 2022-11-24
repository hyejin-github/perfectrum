package com.perfectrum.backend.dto.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserUpdateInfoDto {
    private String profileImg;
    private String nickname;
    private String gender;
    private String seasons;
    private Integer accordClass;
}
