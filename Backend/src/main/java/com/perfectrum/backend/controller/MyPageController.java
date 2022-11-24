package com.perfectrum.backend.controller;

import com.perfectrum.backend.domain.repository.UserAccordClassRepository;
import com.perfectrum.backend.dto.MyPage.HaveListDto;
import com.perfectrum.backend.dto.MyPage.UserAccordClassDto;
import com.perfectrum.backend.dto.MyPage.ViewAccordClassDto;
import com.perfectrum.backend.dto.MyPage.WishListDto;
import com.perfectrum.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MyPageController {
    private static final String success = "SUCCESS";
    private static final String fail = "FAIL";
    private static final String timeOut = "access-token timeout";
    private static HttpStatus status = HttpStatus.NOT_FOUND;

    private JwtService jwtService;
    private WishListService wishListService;
    private HaveListService haveListService;
    private UserAccordClassService userAccordClassService;
    private AccordClassService accordClassService;

    @Autowired
    MyPageController(JwtService jwtService, WishListService wishListService,AccordClassService accordClassService,
                     HaveListService haveListService, UserAccordClassService userAccordClassService) {

        this.jwtService = jwtService;
        this.wishListService = wishListService;
        this.haveListService = haveListService;
        this.userAccordClassService = userAccordClassService;
        this.accordClassService = accordClassService;
    }

    @GetMapping("/my-page/analysis")
    public ResponseEntity<?> viewPieChart(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);

        if(decodeId != null){
            try{
                List<UserAccordClassDto> list = userAccordClassService.viewPieChart(decodeId);
                if(list != null){
                    resultMap.put("accordClassList", list);
                }else{
                    resultMap.put("accordClassList", null);
                }
                resultMap.put("message", success);
                status = HttpStatus.OK;
            }catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/my-page/analysis/{accord_class_idx}")
    public ResponseEntity<?> viewAccordClass(HttpServletRequest request, @PathVariable("accord_class_idx") Integer idx){
        Map<String, Object> resultMap = new HashMap<>();

        try{
            ViewAccordClassDto dto = accordClassService.viewAccordClass(idx);
            resultMap.put("accordClass", dto);
            resultMap.put("message", success);
            status = HttpStatus.OK;
        }catch (Exception e){
            resultMap.put("message", fail);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/my-page/wish") // wishList 조회
    public ResponseEntity<?> viewWishList(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);

        if(decodeId != null){
            try{
                List<WishListDto> list = wishListService.viewWishList(decodeId);
                if(list != null){
                    resultMap.put("wishList", list);
                }else{
                    resultMap.put("wishList", new ArrayList<>());
                }
                resultMap.put("message", success);
                status = HttpStatus.OK;
            }catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @PutMapping("/my-page/wish/{idx}")
    public ResponseEntity<?> moveWishToHave(HttpServletRequest request, @PathVariable Integer idx){
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);

        if(decodeId != null){
            try{
                wishListService.moveWishToHave(decodeId, idx);
                resultMap.put("message", success);
                status = HttpStatus.OK;
            }catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @PutMapping("/my-page/wish/delete/{idx}")
    public ResponseEntity<?> deleteWishList(HttpServletRequest request, @PathVariable Integer idx){
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);

        if(decodeId != null){
            try{
                wishListService.deleteWishList(decodeId, idx);
                resultMap.put("message", success);
                status = HttpStatus.OK;
            }catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }
    @GetMapping("/my-page/have")
    public ResponseEntity<?> viewHaveList(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);

        if(decodeId != null){
            try{
                List<HaveListDto> list = haveListService.viewHaveList(decodeId);
                if(list != null){
                    resultMap.put("haveList", list);
                }else{
                    resultMap.put("haveList", new ArrayList<>());
                }
                resultMap.put("message", success);
                status = HttpStatus.OK;
            }catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @PutMapping("/my-page/have/delete/{idx}")
    public ResponseEntity<?> deleteHaveList(HttpServletRequest request, @PathVariable Integer idx){
        Map<String, Object> resultMap = new HashMap<>();
        String decodeId = checkToken(request, resultMap);

        if(decodeId != null){
            try{
                haveListService.deleteHaveList(decodeId, idx);
                resultMap.put("message", success);
                status = HttpStatus.OK;
            }catch (Exception e){
                resultMap.put("message", fail);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/")
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
