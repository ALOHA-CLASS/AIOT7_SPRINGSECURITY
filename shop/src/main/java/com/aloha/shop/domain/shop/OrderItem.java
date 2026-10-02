package com.aloha.shop.domain.shop;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 주문항목
@Getter 
@Setter 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
@Entity 
@Table(name = "order_item")
public class OrderItem {

  // 주문항목 : 주문 = N : 1
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_no", nullable = false)
  private Orders order;

  // 주문항목 : 상품 = N : 1
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_no", nullable = false)
  private Product product;

  // 주문 시점에서의 상품명과 가격 ✨스냅샷
  @Column(nullable = false, length = 100)
  private String productName;       // 상품명
  @Column(nullable = false)
  private int price;                // 가격
  @Column(nullable = false)
  private int quantity;             // 수량

  // 항목 가격
  public int getSubtotal() {
    return price * quantity;    // 단가 x 수량 
  }

  
}
