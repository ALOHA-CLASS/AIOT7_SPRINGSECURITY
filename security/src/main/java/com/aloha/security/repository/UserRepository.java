package com.aloha.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aloha.security.domain.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
  
}
