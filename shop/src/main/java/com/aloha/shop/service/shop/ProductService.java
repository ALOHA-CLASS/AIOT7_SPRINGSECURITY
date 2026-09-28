package com.aloha.shop.service.shop;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.aloha.shop.domain.shop.Product;
import com.aloha.shop.dto.shop.ProductSearch;

public interface ProductService {

  // 상품 목록
  List<Product> list();

  // 페이징 목록
  Page<Product> list(Pageable pageable);

  // 페이징 목록 + 필터&검색
  Page<Product> list(Pageable pageable, ProductSearch search);

  // 상품 상세 - id
  Product select(String id);

  // 상품 등록
  Product create(Product product);

  // 상품 수정
  Product update(Product product);

  // 상품 삭제
  void delete(String id);
  
}
