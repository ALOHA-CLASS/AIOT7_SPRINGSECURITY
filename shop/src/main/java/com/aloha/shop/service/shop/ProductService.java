package com.aloha.shop.service.shop;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.aloha.shop.domain.shop.Product;

public interface ProductService {

  // 상품 목록
  List<Product> list();

  // 페이징 목록
  Page<Product> list(Pageable pageable);
  
}
