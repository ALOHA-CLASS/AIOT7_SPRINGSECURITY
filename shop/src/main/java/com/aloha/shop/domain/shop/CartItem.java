package com.aloha.shop.domain.shop;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.aloha.shop.domain.BaseEntity;
import com.aloha.shop.domain.users.User;

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

@Getter 
@Setter 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
@Entity 
@Table (name ="cart_item")
public class CartItem extends BaseEntity {
  
  // N:1 = CartItem : User
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_no", nullable = false)
  private User user;
  
  // N:1 = CartItem : Product
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_no", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)  // 외래키 ON DELETE CASCADE 옵션 설정
  private Product product;

  @Builder.Default
  @Column(nullable = false)
  private int quantity = 1;             // 수량
  
}
