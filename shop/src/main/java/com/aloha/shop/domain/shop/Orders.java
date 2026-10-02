package com.aloha.shop.domain.shop;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.aloha.shop.domain.BaseEntity;
import com.aloha.shop.domain.users.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 주문 엔터티
@Getter 
@Setter 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
@Entity 
@Table(name = "orders")
public class Orders extends BaseEntity {
  
  // 주문 : 회원 = N : 1
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_no", nullable = false)
  private User user;

  @Column(nullable = false)
  private int totalAmount;    // 총 결제금액

  // 토스페이먼츠 결제 정보
  @Column(unique = true, length = 100)
  private String orderNo;     // 결제창 연동용 주문번호 (requestPayment의 orderId)
  @Column(length = 200)
  private String paymentKey;  // 토스페이먼츠 결제 승인 키

  // 주문 상태
  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private OrderStatus status = OrderStatus.ORDERED;

  // 배송지 정보 (주문 시점의 주소가 기록이 되어야한다✨)
  @Column(nullable = false, length = 50)
  private String receiver;                    // 받는 사람
  @Column(nullable = false, length = 30)
  private String phone;                       // 연락처
  @Column(nullable = false, length = 10)
  private String zipcode;                     // 우편번호
  @Column(nullable = false, length = 200)
  private String address1;                    // 기본주소
  @Column(nullable = false, length = 200)
  private String address2;                    // 상세주소

  // 주문 : 주문항목 = 1 : N
  @Builder.Default
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> orderItems = new ArrayList<>();

  // 주문항목 추가 메소드
  public void addOrderItem(OrderItem item) {
    item.setOrder(this);
    this.orderItems.add(item);
  }

  // 주문번호 자동 생성
  public static String generateOrderNo() {
    // TODO: 나중에 OOOO_OO_OOOO 규칙이 있는 주문번호 생성을 해보자
    return UUID.randomUUID().toString();
  }

}



