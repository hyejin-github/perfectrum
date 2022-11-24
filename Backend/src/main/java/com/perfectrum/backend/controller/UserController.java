package com.perfectrum.backend.controller;

import com.perfectrum.backend.dto.review.MyReviewDto;
import com.perfectrum.backend.dto.review.MyReviewListDto;
import com.perfectrum.backend.dto.user.UserMoreInfoDto;
import com.perfectrum.backend.dto.user.UserInfoDto;
import com.perfectrum.backend.dto.user.UserUpdateInfoDto;
import com.perfectrum.backend.service.JwtService;
import com.perfectrum.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private static final String success = "SUCCESS";
    private static final String fail = "FAIL";
    private static final String timeOut = "access-token timeout";
    private static HttpStatus status = HttpStatus.NOT_FOUND;

    private final UserService userService;
    private final JwtService jwtService;
    @Autowired
    UserController(UserService userService, JwtService jwtService){
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @GetMapping("/user") // 내 정보 조회
    public ResponseEntity<?> getUserInfo(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);

        if(decodeId != null){
            try{
                UserInfoDto userInfo = userService.getUserInfo(decodeId);
                if(userInfo != null){
                    resultMap.put("data", userInfo);
                    resultMap.put("message", success);
                    status = HttpStatus.OK;
                }else{
                    resultMap.put("message", fail);
                    status = HttpStatus.INTERNAL_SERVER_ERROR;
                }
            }catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @PostMapping("/user") // 추가 정보 입력
    public ResponseEntity<?> addMoreUserInfo(HttpServletRequest request, @RequestBody UserMoreInfoDto userMoreInfoDto){
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);

        if(decodeId != null){
            try{
                userService.addMoreUserInfo(decodeId, userMoreInfoDto);
                resultMap.put("message", success);
                status = HttpStatus.OK;
            }catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/user/check/{nickname}") // 닉네임 체크
    public ResponseEntity<?> checkNickName(@PathVariable String nickname){
        Map<String, Object> resultMap = new HashMap<>();
        try {
            if(userService.checkNickName(nickname)){ // 닉네임 서비스 호출
                resultMap.put("message", fail); // true이면 중복
            } else{
                resultMap.put("message", success);
            }
            status = HttpStatus.OK;
        }catch (Exception e){
            resultMap.put("message", fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @PutMapping("/user") // 회원 정보 수정
    public ResponseEntity<?> updateUserInfo(HttpServletRequest request, @RequestBody UserUpdateInfoDto userUpdateInfoDto){
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);

        if(decodeId != null){
            try{
                UserInfoDto userInfoDto = userService.updateUserInfo(decodeId, userUpdateInfoDto);
                resultMap.put("data", userInfoDto);
                resultMap.put("message", success);
                status = HttpStatus.OK;
            }catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @DeleteMapping("/user") // 회원 탈퇴
    public ResponseEntity<?> deleteUser(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);

        if(decodeId != null){
            try {
                userService.deleteUser(decodeId);
                resultMap.put("message", success);
                status = HttpStatus.OK;
            } catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @PostMapping("/user/reviews/{nickname}") // 사용자가 작성한 리뷰 조회
    public ResponseEntity<?> viewMyReviews(@PathVariable("nickname") String nickname, @RequestBody MyReviewListDto myReviewListDto){
        Map<String, Object> resultMap = new HashMap<>();

        try{
            Map<String, Object> data = userService.viewMyReviews(nickname, myReviewListDto);
            String userId = userService.getUserId(nickname);
            UserInfoDto userInfoDto = userService.getUserInfo(userId);
            resultMap.put("userInfo",userInfoDto);
            resultMap.put("totalReviews", userService.getTotalReviews(nickname));
            resultMap.put("avgReviews", userService.getAvgReviews(nickname));
            resultMap.put("hasNext", data.get("hasNext"));
            resultMap.put("myReviewList", data.get("myReviewList"));
            resultMap.put("message", success);
            status = HttpStatus.OK;
        }catch (Exception e){
            resultMap.put("message", fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }
    @GetMapping("/user/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<>();
        try{
            Cookie oldCookie = null;
            Cookie[] cookies = request.getCookies();
            if(cookies != null){
                for(Cookie c : cookies){
                    if(c.getName().equals("refresh-token")){
                        oldCookie = c;
                    }
                }
            }
            if(oldCookie != null){ // 토큰 제거
                oldCookie.setValue(null);
                oldCookie.setPath("/");
                oldCookie.setMaxAge(0);
                response.addCookie(oldCookie);
            }
            status = HttpStatus.OK;
            resultMap.put("message", success);
        }catch (Exception e){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            resultMap.put("message", fail);
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/user/token") // 엑세스 토큰 재발급
    public ResponseEntity<?> updateAccessToken(HttpServletRequest request, @CookieValue("refresh-token") String refreshToken){
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = jwtService.decodeToken(refreshToken);
        if(!decodeId.equals("timeout")){
            String accessToken = jwtService.createAccessToken("id", decodeId);
            resultMap.put("access-token", accessToken);
            resultMap.put("message", success);
            status = HttpStatus.OK;
        }else {
            resultMap.put("message", "refresh-token timeout");
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(resultMap, status);
    }
    public String checkToken(HttpServletRequest request, Map<String, Object> resultMap){
        String accessToken = request.getHeader("Authorization");
        String decodeId = jwtService.decodeToken(accessToken);
        if(!decodeId.equals("timeout")){
            return decodeId;
        }else{
            resultMap.put("message", timeOut);
            status = HttpStatus.UNAUTHORIZED;
            return null;
        }
    }
}