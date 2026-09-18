package com.aloha.security.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aloha.security.domain.UserAuth;
import com.aloha.security.domain.Users;
import com.aloha.security.repository.UserAuthRepository;
import com.aloha.security.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserAuthRepository userAuthRepository;
  private final PasswordEncoder passwordEncoder;

  // 회원가입
  @Override
  @Transactional 
  public boolean join(Users user) throws Exception {
    // 비밀번호 암호화
    // 123456 --> 🔒 F123456ABC12389517832813
    String encodedPassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(encodedPassword);

    // 회원 등록
    Users saved = userRepository.save(user);

    // 회원 기본 권한 등록
    UserAuth userAuth = UserAuth.builder().auth("ROLE_USER").build();
    saved.addAuth(userAuth);
    userAuthRepository.save(userAuth);

    return saved.getNo() != null;
  }
  
}
