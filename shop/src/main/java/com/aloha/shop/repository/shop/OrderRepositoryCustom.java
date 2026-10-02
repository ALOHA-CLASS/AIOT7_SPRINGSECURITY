package com.aloha.shop.repository.shop;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.aloha.shop.domain.shop.Orders;

public interface OrderRepositoryCustom {

  /**
   * 회원 주문내역
   * SELECT *
   * FROM orders
   * WHERE user_no = ?
   * ORDER BY no DESC
   * @param userNo
   * @param pageable
   * @return
   */
  Page<Orders> findByUser(Long userNo, Pageable pageable);
  
}
