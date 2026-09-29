package com.aloha.shop.repository.shop;

import com.aloha.shop.domain.shop.CartItem;

// QueryDSL 로 동적쿼리 정의
public interface CartItemRepositoryCustom {

  /**
   * 장바구니 ID 로 조회
   * SELECT *
   * FROM cart_item
   * WHERE id = ?
   */
  CartItem findById(String id);

  /**
   * DELETE FROM cart_item
   * WHERE id = ?
   * @param id
   */
  void deleteById(String id);
  
}
