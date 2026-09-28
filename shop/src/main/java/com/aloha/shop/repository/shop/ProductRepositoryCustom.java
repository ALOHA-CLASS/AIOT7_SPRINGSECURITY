package com.aloha.shop.repository.shop;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.aloha.shop.domain.shop.Product;
import com.aloha.shop.dto.shop.ProductSearch;

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

  /**
   * 상품 페이징 목록 + 필터&검색
   * SELECT *
   * FROM product
   * WHERE <필터&검색 조건> 
   * LIMIT 시작, 개수
   * @param pageable
   * @param search
   * @return
   */
  Page<Product> page(Pageable pageable, ProductSearch search);

  /**
   * 상품 ID 로 조회
   * SELECT *
   * FROM product
   * WHERE id = ?
   */
  Product findById(String id);


  

  
}
