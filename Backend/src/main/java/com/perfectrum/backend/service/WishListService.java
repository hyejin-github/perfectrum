package com.perfectrum.backend.service;

import com.perfectrum.backend.dto.MyPage.WishListDto;

import java.util.List;

public interface WishListService {
    List<WishListDto> viewWishList(String decodeId);

    void moveWishToHave(String decodeId, Integer idx);

    void deleteWishList(String decodeId, Integer idx);
}
