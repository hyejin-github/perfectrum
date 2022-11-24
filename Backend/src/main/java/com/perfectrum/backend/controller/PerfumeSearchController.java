package com.perfectrum.backend.controller;


import com.perfectrum.backend.domain.entity.PerfumeEntity;
import com.perfectrum.backend.dto.Search.PerfumeSearchDto;
import com.perfectrum.backend.dto.review.ReviewListDto;
import com.perfectrum.backend.dto.review.ReviewRegistDto;
import com.perfectrum.backend.dto.review.ReviewViewDto;
import com.perfectrum.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class PerfumeSearchController {

    private static final String success = "SUCCESS";
    private static final String fail = "FAIL";
    private static final String timeOut = "access-token timeout";
    private static HttpStatus status = HttpStatus.NOT_FOUND;

    private JwtService jwtService;
    private WishListService wishListService;
    private HaveListService haveListService;

    private SearchService perfumeSearchService;
    private PerfumeDetailService perfumeDetailService;

    @Autowired
    PerfumeSearchController(JwtService jwtService, WishListService wishListService,
                            HaveListService haveListService,PerfumeDetailService perfumeDetailService,
                            SearchService searchService){
        this.jwtService = jwtService;
        this.wishListService = wishListService;
        this.haveListService = haveListService;
        this.perfumeDetailService = perfumeDetailService;
        this.perfumeSearchService = searchService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> getSearchResult(HttpServletRequest request, @RequestBody PerfumeSearchDto perfumeSearchDto){
        Map<String,Object> resultMap = new HashMap<>();
        String decodeId = "isLogin";

        if(request != null && request.getHeader("Authorization") != null){
            decodeId = checkToken(request, resultMap);
        }

        if(decodeId != null){
            try{
                Map<String,Object> data = perfumeSearchService.searchPerfume(decodeId,perfumeSearchDto);
                if(data.get("perfumeList")!=null){
                    resultMap.put("searchList", data.get("perfumeList"));
                    resultMap.put("hasNext", data.get("hasNext"));
                    resultMap.put("isSearched",true);
                }else{
                    resultMap.put("searchList",data.get("bestPerfumeList"));
                    resultMap.put("isSearched",false);
                }
                resultMap.put("message",success);
                status = HttpStatus.OK;
            }catch(Exception e){
                resultMap.put("message",fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }

        }

        return new ResponseEntity<>(resultMap,status);
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