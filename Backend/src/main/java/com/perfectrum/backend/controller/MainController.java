package com.perfectrum.backend.controller;

import com.perfectrum.backend.domain.entity.PerfumeEntity;
import com.perfectrum.backend.dto.MyPage.ViewAccordClassDto;
import com.perfectrum.backend.dto.perfume.AccordMoreInfoDto;
import com.perfectrum.backend.dto.perfume.PerfumeViewDto;
import com.perfectrum.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MainController {
    private static final String success = "SUCCESS";
    private static final String fail = "FAIL";
    private static final String timeOut = "access-token timeout";
    private static HttpStatus status = HttpStatus.NOT_FOUND;

    private UserService userService;
    private JwtService jwtService;
    private PerfumeService perfumeService;
    private AccordService accordService;
    private AccordClassService accordClassService;


    @Autowired
    MainController(UserService userService, JwtService jwtService, PerfumeService perfumeService, AccordService accordService, AccordClassService accordClassService){
        this.jwtService = jwtService;
        this.userService = userService;
        this.perfumeService = perfumeService;
        this.accordService = accordService;
        this.accordClassService = accordClassService;
    }


    @GetMapping("/main") // 베스트 향수 조회
    public ResponseEntity<?> viewBestPerfume(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = "isLogin";

        if(request != null & request.getHeader("Authorization") != null){
            decodeId = checkToken(request, resultMap);
        }

        // 토큰만료안됨 -> 로그인 했거나 비로그인 상태
        if(decodeId != null){
            try{
                List<PerfumeViewDto> bestPerfumeList = perfumeService.viewBestPerfume(decodeId); // 사용자 정보 기반 베스트 향수
                resultMap.put("BestPerfumeList", bestPerfumeList);
                List<PerfumeViewDto> todayPerfumeList = perfumeService.viewTodayPerfume(decodeId); // 오늘의 향수 추천

                resultMap.put("todayPerfumeList", todayPerfumeList);
                resultMap.put("message", success);


                status = HttpStatus.OK;
            }catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/main/docs/{accord_class_idx}") // 향수도감 api
    public ResponseEntity<?> viewAccordDocs(@PathVariable("accord_class_idx") Integer idx){
        Map<String, Object> resultMap = new HashMap<>();

        try{
            List<AccordMoreInfoDto> accordList = accordService.viewAccordDocs(idx);
            ViewAccordClassDto dto = accordClassService.viewAccordClass(idx);
            resultMap.put("accordClass", dto);
            resultMap.put("accordList", accordList);
            resultMap.put("message",success);
            status = HttpStatus.OK;
        }catch (Exception e){
            resultMap.put("message", fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
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
