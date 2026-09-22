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

// 회원 권한 엔터티
@Getter 
@Setter 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
@Entity 
@Table(name = "user_auth")
public class UserAuth extends BaseEntity {

  @Column(nullable = false, length = 50)
  private String username;          // 회원 아이디

  @Column(nullable = false, length = 30)
  private String auth;              // 회원 권한

  // 회원 권한 : 회원 = N : 1
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_no")
  private User user;

  
}
