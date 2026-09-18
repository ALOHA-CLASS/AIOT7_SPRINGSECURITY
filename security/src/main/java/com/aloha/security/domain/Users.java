package com.aloha.security.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity 
@Table(name = "user")
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
@ToString(exclude = "authList")
public class Users extends BaseEntity {

  @Column(nullable = false, unique = true, length = 100)
  private String username;

  @Column(nullable = false, length = 200)
  private String password;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(length = 200)
  private String email;

  @Builder.Default              // 빌더 패턴으로 객체 생성 시, 초기화한 값을 넣어준다.
  @Column(nullable = false)
  private int enabled = 1;

  // 회원 권한 목록 (Users : UserAuth = 1 : N)
  // cascade = CascadeType.ALL  : ON UPDATE CASCADE
  // ➡ 부모 Users 의 PK(no) 변경되면, 자식 UserAuth FK(user_no) 변경합니다.

  // fetch = FetchType.LAZY     : ON UPDATE CASCADE
  // ➡ 부모 Users 의 데이터가 삭제되면, 자식 UserAuth 의 데이터도 삭제합니다.
  // fetch = FetchType.LAZY
  // ➡ LAZY : Users 를 조회해도 아직 List<UserAuth> 조회되지 않는다.
  //           authList 를 사용하려고 하면 그때 List<UserAuth> 을 조회한다.
  // ➡ EAGER : Users 조회할 때 바로 List<UserAuth> 을 조회한다.

  // orphanRemoval = true : 부모 자식 관계가 끊기면, 자식 UserAuth 데이터를 삭제한다.
  @Builder.Default
  @OneToMany(
    mappedBy = "user", cascade = CascadeType.ALL, 
    fetch = FetchType.LAZY, orphanRemoval = true
  )
  private List<UserAuth> authList = new ArrayList<>();

  // 회원 추가 메소드
  public void addAuth(UserAuth userAuth) {
    userAuth.setUser(this);
    this.authList.add(userAuth);
  }
}
