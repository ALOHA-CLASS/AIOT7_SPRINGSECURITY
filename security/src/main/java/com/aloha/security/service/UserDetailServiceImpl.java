package com.aloha.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.aloha.security.domain.Users;
import com.aloha.security.dto.CustomUser;
import com.aloha.security.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 🔐 UserDetailService : 사용자 정보 불러오는 인터페이스
 * ✅ 이 인터페이스를 구현하여, 사용자 정보를 로드하는 방법을 정의할 수 있습니다.
 */
@Slf4j 
@Service 
@RequiredArgsConstructor 
public class UserDetailServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("::::: UserDetailServiceImpl :::::");
    log.info("- 사용자 정의 인증을 위해, 사용자 정보 조회");
    log.info("- username : {}", username);

    Users user = userRepository.findByUsernameWithAuth(username)
                                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    

    // 🔐 CustomUser ➡ UserDetails
    return new CustomUser(user);
  }

  
  
}
