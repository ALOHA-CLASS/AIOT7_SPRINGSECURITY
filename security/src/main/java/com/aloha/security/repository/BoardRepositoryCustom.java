package com.aloha.security.repository;

import java.util.List;
import java.util.Optional;

import com.aloha.security.domain.Board;

/**
 *  QueryDSL 을 이용한 게시글 조회 기능
 */
public interface BoardRepositoryCustom {

  // 목록 - 작성자(회원) 정보와 조인하여 조회
  List<Board> findAllWithUser();

  // 조회 - id(UUID), 작성자(회원)와 조인하여 조회
  Optional<Board> findByWithUser(String id);

  // ⭐ 게시글 작성자 본인 여부 확인
  boolean isOwner(String id, Long userNo);
  
}
