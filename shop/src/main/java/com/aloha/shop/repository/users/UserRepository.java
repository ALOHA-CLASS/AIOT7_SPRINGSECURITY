package com.aloha.shop.repository.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aloha.shop.domain.users.User;

public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * 아이디로 조회
   * SELECT *
   * FROM users
   * WHERE username = ?
   */
  Optional<User> findByUsername(String username);

  /**
   * 아이디로 존재 여부 확인
   * SELECT COUNT(*)
   * FROM users
   * WHERE username = ?
   */
  boolean existsByUsername(String username);

  
}