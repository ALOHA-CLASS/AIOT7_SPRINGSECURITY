package com.aloha.shop.service.shop;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.aloha.shop.domain.shop.Orders;
import com.aloha.shop.dto.shop.OrderForm;

public interface OrderService {
  
  // 상품상세에서 바로 주문 (상품1건)
  Orders orderDirect(OrderForm orderForm);
  
  // 장바구니 전체 주문
  Orders orderCart(OrderForm orderForm);

  // 주문 내역
  Page<Orders> list(Long userNo, Pageable pageable);

  // 주문 상세 조회
  Orders select(Long userNo, Long no);
  
  // 주문 취소
  void cancel(Long userNo, Long no);


}
