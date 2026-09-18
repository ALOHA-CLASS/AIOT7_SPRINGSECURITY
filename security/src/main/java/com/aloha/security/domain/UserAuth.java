package com.aloha.security.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Entity 
@Table(name = "user_auth" )
@Data 
@Builder
@ToString(exclude = "user")
public class UserAuth extends BaseEntity {

  // Users : UserAuth = 1 : N
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_no", nullable = false)
  private Users user;

  @Column(nullable = false, length = 100)
  private String auth;            // ⭐ 회원 권한 : ROLE_USER, ROLE_ADMIN 등
  
}
