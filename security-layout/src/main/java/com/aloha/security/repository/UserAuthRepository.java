package com.aloha.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aloha.security.domain.UserAuth;

public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {
  
}
