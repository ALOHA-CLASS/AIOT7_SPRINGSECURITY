package com.aloha.shop.repository.shop;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aloha.shop.domain.shop.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long>, OrderRepositoryCustom {

  // 결제창 연동용 주문번호로 조회
  Optional<Orders> findByOrderNo(String orderNo);

}
