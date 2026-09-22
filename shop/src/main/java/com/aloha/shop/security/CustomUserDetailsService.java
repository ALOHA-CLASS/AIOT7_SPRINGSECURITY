package com.aloha.shop.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aloha.shop.domain.users.User;
import com.aloha.shop.repository.users.UserRepository;

import lombok.RequiredArgsConstructor;

// 사용자 정의 인증
@Service 
@RequiredArgsConstructor 
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  // 아이디로 회원 정보 조회
  @Override
  @Transactional 
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. - " + username));
    user.getAuthList().size();                
    return new CustomUser(user);
  }  

}





