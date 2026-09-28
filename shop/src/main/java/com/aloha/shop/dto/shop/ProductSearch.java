package com.aloha.shop.dto.shop;

import lombok.Data;

@Data 
public class ProductSearch {
  private String keyword;     // 검색어
  private String category;    // 카테고리
}
