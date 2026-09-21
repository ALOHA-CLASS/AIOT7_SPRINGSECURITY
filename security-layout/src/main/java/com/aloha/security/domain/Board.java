package com.aloha.security.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity 
@Table(name = "board")
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
@ToString(exclude = "user")
public class Board extends BaseEntity {

  @Column(nullable = false, length = 100)
  private String title;

  @Lob      // TEXT 데이터 타입
  private String content;

  // Board : Users = N : 1
  @ManyToOne(fetch = FetchType.LAZY) 
  @JoinColumn(name = "user_no", nullable = false)
  private Users user;
  
}
