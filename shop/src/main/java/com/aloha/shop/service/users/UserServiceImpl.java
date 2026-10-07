package com.aloha.shop.service.users;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aloha.shop.domain.users.User;
import com.aloha.shop.dto.users.UserJoinDto;
import com.aloha.shop.dto.users.UserUpdateDto;
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

  @Override
  public User select(String username) {
    User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    return user;
  }

  @Override
  @Transactional 
  public User update(String username, UserUpdateDto dto) {
    User user = select(username);
    user.setName(dto.getName());
    user.setEmail(dto.getEmail());
    user.setPhone(dto.getPhone());
    
    return userRepository.save(user);
  }

  @Override
  public boolean checkPassword(String username, String passwrod) {
    // 아이디로 회원 정보 조회
    User user = select(username);
    // 저장되어 있는 암호화된 비밀번호
    String encodedPassword = user.getPassword();
    // 암호화 알고리즘으로 평문:암호문 = 123456 : AB38238EF34834F 일치 여부
    boolean result = passwordEncoder.matches(passwrod, encodedPassword);
    return result;
  }

  @Override
  @Transactional 
  public User changePassword(String username, String newPassword) {
    // 아이디로 회원 조회
    User user = select(username);

    // 비밀번호 암호화
    String encodedPassword = passwordEncoder.encode(newPassword);
    user.setPassword(encodedPassword);
    return userRepository.save(user);
  }

  
  
}
