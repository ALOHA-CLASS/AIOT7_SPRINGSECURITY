package com.aloha.shop.repository.shop;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aloha.shop.domain.shop.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long>, OrderRepositoryCustom {
  
}
