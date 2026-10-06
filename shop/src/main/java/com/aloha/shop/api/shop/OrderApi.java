package com.aloha.shop.api.shop;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aloha.shop.security.CustomUser;
import com.aloha.shop.service.shop.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderApi {

  private final OrderService orderService;

  // 주문 취소
  @PutMapping("/{no}/cancel")
  public ResponseEntity<?> cancel(
    @AuthenticationPrincipal CustomUser loginUser,
    @PathVariable("no") Long no
  ) {
    try {
      Long userNo = loginUser.getUser().getNo();
      orderService.cancel(userNo, no);
      return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>("FAIL", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
}
