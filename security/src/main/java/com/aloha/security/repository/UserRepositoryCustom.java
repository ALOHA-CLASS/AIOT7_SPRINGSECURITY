package com.aloha.security.repository;

import java.util.Optional;

import com.aloha.security.domain.Users;

/**
 * QueryDSL 이용한 회원 조회 기능
 */
public interface UserRepositoryCustom {
  
  // 회원+권한목록 LEFT JOIN 조회
  Optional<Users> findByUsernameWithAuth(String username);

}
