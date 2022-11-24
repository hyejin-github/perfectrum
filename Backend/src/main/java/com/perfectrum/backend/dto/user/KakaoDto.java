package com.perfectrum.backend.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoDto {
    private Long id;
    private Properties properties;
    private KakaoAccount kakao_account;
    private String test;

    @Getter
    @ToString
    public static class KakaoAccount{
        private String email;
    }

    @Getter
    @ToString
    public static class Properties{
        private String nickname;
        private String thumbnail_image;
    }
}
