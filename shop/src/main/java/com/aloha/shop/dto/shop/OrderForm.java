package com.aloha.shop.dto.shop;

import lombok.Data;

// 주문서 작성 폼 DTO
@Data 
public class OrderForm {
  private Long userNo;          // 회원번호
  private Long productNo;       // 상품번호
  private int quantity;         // 수량

  // 배송지 정보 스냅샷
  private String receiver;  
  private String phone;  
  private String zipcode; 
  private String address1; 
  private String address2;  
}
