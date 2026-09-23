package com.aloha.shop.domain.shop;

import com.aloha.shop.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
@Entity 
@Table(name = "product")
@ToString 
public class Product extends BaseEntity {

  @Column(nullable = false, length = 100)
  private String name;            // 상품명
  
  @Column(length = 50)
  private String category;        // 카테고리

  @Column(nullable = false)
  private int price;              // 가격

  @Builder.Default
  @Column(nullable = false)
  private int stock = 0;          // 재고
  
  @Column(length = 1000)
  private String description;     // 설명

  @Column(length = 500)
  private String imageUrl;        // 이미지 경로
  
}
