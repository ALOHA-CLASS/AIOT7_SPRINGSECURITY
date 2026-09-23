package com.aloha.shop.repository.shop;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aloha.shop.domain.shop.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

  
  
}
