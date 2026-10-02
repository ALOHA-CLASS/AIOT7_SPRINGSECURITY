package com.aloha.shop.repository.shop;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.aloha.shop.domain.shop.CartItem;

/**
 * 장바구니 데이터베이스 접근 Repository
 *
 * JpaRepository<CartItem, Long>을 상속하면
 * 기본적인 CRUD 기능을 자동으로 제공합니다.
 *
 * 또한 메서드 이름에 정해진 규칙을 사용하면
 * SQL/JPQL을 직접 작성하지 않아도
 * Spring Data JPA가 쿼리를 자동으로 생성합니다.
 *
 * 예)
 * findByUserNo()
 * → SELECT * FROM cart_item WHERE user_no = ?
 *
 * countByUserNo()
 * → SELECT COUNT(*) FROM cart_item WHERE user_no = ?
 */
public interface CartItemRepository extends JpaRepository<CartItem, Long>, CartItemRepositoryCustom {
  
  // 장바구니 목록 - 페이징
  Page<CartItem> findByUserNo(Long userNo, Pageable pageable);

  // 장바구니 상품 개수
  long countByUserNo(Long userNo);

  // 전체 목록
  List<CartItem> findByUserNo(Long userNo);

  // 장바구니 비우기
  void deleteByUserNo(Long userNo);

  
}
