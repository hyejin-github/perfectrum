package com.perfectrum.backend.controller;

import com.perfectrum.backend.service.JwtService;
import com.perfectrum.backend.service.KakaoUserService;
import com.perfectrum.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SocialController {
    private static final String success = "SUCCESS";
    private static final String fail = "FAIL";
    private static final String timeOut = "access-token tieout";
    private static HttpStatus status = HttpStatus.NOT_FOUND;

    private final UserService userService;
    private final KakaoUserService kakaoUserService;
    private final JwtService jwtService;

    @Autowired
    SocialController(UserService userService, KakaoUserService kakaoUserService, JwtService jwtService){
        this.userService = userService;
        this.kakaoUserService = kakaoUserService;
        this.jwtService = jwtService;
    }

    @GetMapping("/oauth/kakao")
    public ResponseEntity<?> getKakaoAuthCode(@RequestParam String code, HttpServletResponse response) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        String kakaoToken = kakaoUserService.getKakaoAccessToken(code); // 인가코드로 카카오토큰 발급
        try{
            String[] res = kakaoUserService.createKakaoUser(kakaoToken); // 카카오 토근으로 카카오 회원 정보 조회
            if(res!=null){
                // 로그인 됨 -> 엑세스토큰, 리프레시토큰 발급
                String accessToken = jwtService.createAccessToken("id", res[0]);
                String refreshToken = jwtService.createRefreshToken("id", res[0]);

                resultMap.put("isRegist", res[1]); // true -> 새로 가입한 회원, false -> 이미 가입된 회원
                resultMap.put("access-token", accessToken);

                // 리프레쉬 토큰은 쿠키에 저장
                Cookie refreshCookie = new Cookie("refresh-token", refreshToken);
                refreshCookie.setMaxAge(1*60*60);
                refreshCookie.setPath("/");
                refreshCookie.setHttpOnly(true);

                response.addCookie(refreshCookie);

                resultMap.put("message", success);
                status = HttpStatus.OK;
            }else{
                resultMap.put("message", fail);
                status = HttpStatus.UNAUTHORIZED;
            }
        }catch (Exception e){
            resultMap.put("message", fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/kakao")
    public ResponseEntity<?> authKakao(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String authToken = request.getHeader("authToken"); // 프론트에서 인가코드 헤더에 실어서 보냄
        try{
            String[] res = kakaoUserService.createKakaoUser(authToken); // 카카오 토근으로 카카오 회원 정보 조회
            if(res!=null){
                // 로그인 됨 -> 엑세스토큰, 리프레시토큰 발급
                String accessToken = jwtService.createAccessToken("id", res[0]);
                String refreshToken = jwtService.createRefreshToken("id", res[0]);

                resultMap.put("isRegist", res[1]); // true -> 새로 가입한 회원, false -> 이미 가입된 회원
                resultMap.put("access-token", accessToken);

                // 리프레쉬 토큰은 쿠키에 저장
                Cookie refreshCookie = new Cookie("refresh-token", refreshToken);
                refreshCookie.setMaxAge(1*60*60);
                refreshCookie.setPath("/");
                refreshCookie.setHttpOnly(true);

                response.addCookie(refreshCookie);

                resultMap.put("message", success);
                status = HttpStatus.OK;
            }else{
                resultMap.put("message", fail);
                status = HttpStatus.UNAUTHORIZED;
            }
        }catch (Exception e){
            resultMap.put("message", fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }
}