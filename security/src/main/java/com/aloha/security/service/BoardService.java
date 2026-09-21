package com.aloha.security.service;

import java.util.List;

import com.aloha.security.domain.Board;

public interface BoardService {
  // 목록
  List<Board> list() throws Exception;
  // 조회
  Board selectById(String id) throws Exception;
  // 등록
  boolean insert(Board board, Long userNo) throws Exception;
  // 수정 
  boolean updateById(Board board) throws Exception;
  // 삭제
  boolean deleteById(String id) throws Exception;
  // 소유자 확인
  boolean isOwner(String id, Long userNo) throws Exception;
}
