package com.aloha.shop.service.users;

import com.aloha.shop.domain.users.User;
import com.aloha.shop.dto.users.UserJoinDto;
import com.aloha.shop.dto.users.UserUpdateDto;

public interface UserService {

  // 아이디 사용 가능 여부 (아이디 중복 검사)
  boolean isUsernameAvailable(String username);

  // 회원 가입
  User join(UserJoinDto dto);

  // 회원 조회
  User select(String username);

  // 회원 수정
  User update(String username, UserUpdateDto dto);

  // 현재 비밀번호 일치 여부 
  boolean checkPassword(String username, String passwrod);

  // 비밀번호 변경
  User changePassword(String username, String newPassword);


  
}
