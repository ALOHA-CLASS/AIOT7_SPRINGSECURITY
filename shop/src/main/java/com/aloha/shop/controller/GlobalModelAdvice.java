package com.aloha.shop.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.aloha.shop.security.CustomUser;
import com.aloha.shop.service.shop.CartItemService;

import lombok.RequiredArgsConstructor;

@ControllerAdvice     // 여러 Controller에 공통으로 적용할 기능을 한 곳에서 처리하는 어노테이션
@RequiredArgsConstructor 
public class GlobalModelAdvice {

  private final CartItemService cartItemService;

  // 전체 컨트롤러의 Model 에 cartCount 라는 이름으로 데이터가 담김
  @ModelAttribute("cartCount")
  public long cartCount(
    @AuthenticationPrincipal CustomUser loginUser
  ) {
    if(loginUser == null) {
      return 0;
    }
    // 회원의 장바구니 개수 조회
    Long userNo = loginUser.getUser().getNo();
    long cartCount = cartItemService.count(userNo); 
    return cartCount;
  }
  
}
