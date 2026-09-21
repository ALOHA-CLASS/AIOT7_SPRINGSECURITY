package com.aloha.security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.aloha.security.domain.Board;
import com.aloha.security.domain.QBoard;
import com.aloha.security.domain.QUsers;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository 
@RequiredArgsConstructor 
public class BoardRepositoryImpl implements BoardRepositoryCustom  {

  private final JPAQueryFactory queryFactory; 

  private static final QBoard board = QBoard.board;     // Board 의 Q타입의 인스턴스
  private static final QUsers user = QUsers.users;      // Users 의 Qu타입의 인스턴스

  @Override
  public List<Board> findAllWithUser() {
    return queryFactory
          .selectFrom(board)
          .join(board.user, user).fetchJoin()
          .orderBy(board.no.desc())
          .fetch();
  }
  
  @Override
  public Optional<Board> findByWithUser(String id) {
    Board result = queryFactory
          .selectFrom(board)
          .join(board.user, user).fetchJoin()
          .where(board.id.eq(id))
          .fetchOne();
    return Optional.ofNullable(result);
  }

  /*
    @param id       : 게시글 id
    @param userNo   : 로그인한 사용자 번호
   */
  @Override
  public boolean isOwner(String id, Long userNo) {
    Long no = queryFactory
                .select(board.no)
                .from(board)
                .where(
                  board.id.eq(id),
                  board.user.no.eq(userNo)  // 작성자 회원번호와 비교
                )
                .fetchFirst();
    return no != null;
  }

  

  
}