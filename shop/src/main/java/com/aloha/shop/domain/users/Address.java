package com.aloha.shop.domain.users;

import com.aloha.shop.domain.BaseEntity;

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

// 배송지 엔터티
@Getter 
@Setter 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
@Entity 
@Table(name = "address")
public class Address extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_no", nullable = false)
  private User user;                          // 회원

  @Column(nullable = false, length = 50)
  private String name;                        // 배송지 이름

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

  @Builder.Default
  @Column(nullable = false)
  private boolean defaultAddress = false;     // 기본 배송지 여부
  
}
