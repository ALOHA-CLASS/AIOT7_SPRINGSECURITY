package com.aloha.security.dto;

import java.text.Collator;
import java.util.Collection;
import java.util.stream.Collectors;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.aloha.security.domain.Users;

import lombok.Getter;
import lombok.ToString;

@Getter 
@ToString 
public class CustomUser implements UserDetails {

  // 회원 정보
  private Users user;

  public CustomUser(Users user) {
    this.user = user;
  }

  /**
   * 🔐 권한 정보 메소드
   * ✅ UserDetails 를 Customer 로 구현하여,
   *    Spring Security 의 User 대신 사용자 정의 인증 객체(Customer)로 적용
   * ⚠ CustomUser 적용 시, 권한을 사용할 때는 'ROLE_' 붙여서 사용해야한다.
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return user.getAuthList().stream()
                            .map( (auth) -> new SimpleGrantedAuthority(auth.getAuth()))
                            .collect(Collectors.toList());  
    // user 의 authList 를 스트림으로 반복하여
    // SimpleGrantedAuthority 객체로 매핑하고
    // List 형식으로 변환하여 반환
  }

  @Override
  public @Nullable String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public boolean isEnabled() {
    return user.getEnabled() == 0 ? false : true;
  }

  
  
}
