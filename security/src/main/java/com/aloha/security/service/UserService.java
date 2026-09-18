package com.aloha.security.service;

import com.aloha.security.domain.Users;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

  // 회원 가입
  public boolean join(Users user) throws Exception;

  // 회원 조회
  public Users select(String username) throws Exception;

  // 로그인
  public boolean login(Users user, HttpServletRequest request) throws Exception;
  
}
