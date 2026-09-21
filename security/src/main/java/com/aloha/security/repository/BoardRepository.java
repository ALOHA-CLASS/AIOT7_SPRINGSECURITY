package com.aloha.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aloha.security.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

  // id(UUID) 로 조회
  Board findById(String id);
  
}
