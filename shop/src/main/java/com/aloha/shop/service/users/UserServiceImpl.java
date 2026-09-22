package com.aloha.shop.service.users;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aloha.shop.domain.users.User;
import com.aloha.shop.dto.users.UserJoinDto;
import com.aloha.shop.repository.users.UserRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public boolean isUsernameAvailable(String username) {
    return !userRepository.existsByUsername(username);
  }

  @Override
  @Transactional 
  public User join(UserJoinDto dto) {
    // 아이디 중복 확인
    if( userRepository.existsByUsername(dto.getUsername()) ) {
      throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
    }
    User user = User.builder()
                    .username(dto.getUsername())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .name(dto.getName())
                    .email(dto.getEmail())
                    .phone(dto.getPhone())
                    .enabled(true)
                    .build();
    // 기본 사용자 권한 추가
    user.addAuth("ROLE_USER");
    // 회원 정보 등록
    return userRepository.save(user);
  }

  
  
}
