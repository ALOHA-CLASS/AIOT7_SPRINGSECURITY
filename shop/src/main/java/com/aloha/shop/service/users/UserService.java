package com.aloha.shop.service.users;

import com.aloha.shop.domain.users.User;
import com.aloha.shop.dto.users.UserJoinDto;

public interface UserService {

  // 아이디 사용 가능 여부 (아이디 중복 검사)
  boolean isUsernameAvailable(String username);

  // 회원 가입
  User join(UserJoinDto dto);

  
}
