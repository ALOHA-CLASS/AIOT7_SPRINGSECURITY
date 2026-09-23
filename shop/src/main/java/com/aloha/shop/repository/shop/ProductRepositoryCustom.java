package com.aloha.shop.repository.shop;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.aloha.shop.domain.shop.Product;

public interface ProductRepositoryCustom {
  
  /**
   * 상품 페이징 목록
   * SELECT *
   * FROM product
   * LIMIT 시작, 개수
   * @param pageable
   * @return
   */
  Page<Product> page(Pageable pageable);
  
}
