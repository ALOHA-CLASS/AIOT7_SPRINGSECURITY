package com.aloha.security.service;

import com.aloha.security.domain.Users;

public interface UserService {

  // 회원 가입
  public boolean join(Users user) throws Exception;
  
}
