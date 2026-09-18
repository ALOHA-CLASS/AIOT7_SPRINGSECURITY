package com.aloha.security.repository;

import java.lang.StackWalker.Option;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.aloha.security.domain.QUserAuth;
import com.aloha.security.domain.QUsers;
import com.aloha.security.domain.Users;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository 
@RequiredArgsConstructor 
public class UserRepositoryImpl implements UserRepositoryCustom {

  private final JPAQueryFactory queryFactory;
  private static final QUsers user = QUsers.users;                // Users Q클래스
  private static final QUserAuth userAuth = QUserAuth.userAuth;   // UserAuth Q클래스

  @Override
  public Optional<Users> findByUsernameWithAuth(String username) {
    Users result = queryFactory
                    .selectFrom(user)
                    .distinct()
                    .leftJoin(user.authList, userAuth).fetchJoin()
                    .where(user.username.eq(username))
                    .fetchOne();
    // SELECT * FROM user u
    // LEFT JOIN user_auth ua ON u.no = ua.user_no
    // WHERE username = ?
    return Optional.ofNullable(result);
  }

  

}
