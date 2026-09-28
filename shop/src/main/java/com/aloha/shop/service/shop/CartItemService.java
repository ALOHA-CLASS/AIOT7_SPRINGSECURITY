package com.aloha.shop.service.shop;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.aloha.shop.domain.shop.CartItem;

public interface CartItemService {

  // 장바구니 페이징 목록
  Page<CartItem> list(Long userNo, Pageable pageable);

  // 장바구니 전체 목록
  List<CartItem> listAll(Long userNo);

  // 장바구니 개수
  long count(Long userNo);

  // 총 주문 금액
  int totalPrice(Long userNo);
  
}
