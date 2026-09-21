package com.aloha.security.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aloha.security.domain.Board;
import com.aloha.security.domain.Users;
import com.aloha.security.repository.BoardRepository;
import com.aloha.security.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Service("BoardService")  // 빈이름 : BoardService
                          // ⭐ @PreAuthorize(" @BoardService.isOwner() ") 형태로 사용 가능
@RequiredArgsConstructor 
public class BoardServiceImpl implements BoardService {

  private final BoardRepository boardRepository;
  private final UserRepository userRepository;

  @Override
  public List<Board> list() throws Exception {
    return boardRepository.findAllWithUser();
  }

  @Override
  public Board selectById(String id) throws Exception {
    return boardRepository.findByWithUser(id).orElse(null);
  }

  @Override
  @Transactional 
  public boolean insert(Board board, Long userNo) throws Exception {
    // 해당 회원정보 조회, 회원을 게시글의 작성자로 연결
    // findById()           : 실제 데이터 조회
    // getReferenceById()   : 참조 객체만 조회 (FK 연결만 할 때)
    Users userRef = userRepository.getReferenceById(userNo);
    board.setUser(userRef);

    Board saved = boardRepository.save(board);
    return saved.getNo() != null;
  }

  @Override
  @Transactional 
  public boolean updateById(Board board) throws Exception {
    Board target = boardRepository
                    .findByWithUser(board.getId())
                    .orElseThrow(() -> new Exception("게시글을 찾을 수 없습니다."));
    
    String title = board.getTitle();
    String content = board.getContent();
    if( title != null && !title.isEmpty() ) {
      target.setTitle(title);
    }
    target.setContent(content);

    boardRepository.save(target);
    return true;
  }

  @Override
  public boolean deleteById(String id) throws Exception {
    Board target = boardRepository
                    .findByWithUser(id)
                    .orElseThrow(() -> new Exception("게시글을 찾을 수 없습니다."));
    boardRepository.delete(target);
    return true;
  }

  /**
   * @param id          : 게시글 id (board-id)
   * @param userNo      : 회원 no   (user-no)
   * 게시글 id 로 작성자 userNo 를 QueryDSL 로 조회하여,
   * 인증된(로그인) 사용자의 no 와 일치하는지 확인
   */
  @Override
  public boolean isOwner(String id, Long userNo) throws Exception {
    log.info("게시글 id : {}", id);
    log.info("로그인한 회원 no : {}", userNo);
    return boardRepository.isOwner(id, userNo);
  }

  
  
}
