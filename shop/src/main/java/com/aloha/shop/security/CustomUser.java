package com.aloha.shop.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.aloha.shop.domain.users.User;

import jakarta.annotation.Nullable;
import lombok.Getter;

// Spring Security 인증 사용자
@Getter 
public class CustomUser implements UserDetails {

  private final User user;

  public CustomUser(User user) {
    this.user = user;
  }

  // 회원 권한을 가져오는 메소드
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return user.getAuthList().stream()
                .map( r -> new SimpleGrantedAuthority( r.getAuth() ) )
                .map( a -> (GrantedAuthority) a )
                .toList();
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
    return user.isEnabled();
  }

  public Long getNo() {
    return user.getNo();
  }

  public String getName() {
    return user.getName();
  }

  

}
