package com.aloha.shop.repository.shop;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.aloha.shop.domain.shop.Orders;
import com.aloha.shop.domain.shop.QOrders;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository 
@RequiredArgsConstructor 
public class OrderRepositoryImpl implements OrderRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Orders> findByUser(Long userNo, Pageable pageable) {
    QOrders orders = QOrders.orders;

    List<Orders> content = queryFactory
                            .selectFrom(orders)
                            .where(orders.user.no.eq(userNo))
                            .orderBy(orders.no.desc())
                            .offset(pageable.getOffset())
                            .limit(pageable.getPageSize())
                            .fetch();
    /*
      SELECT *
      FROM orders
      WHERE user_no = ?
      ORDER BY no DESC
      LIMIT offset, size        
      (0, 10)   1페이지
      (10, 10)  2페이지
    */

   // 전체 데이터 개수
   Long total = queryFactory
                    .select(orders.count())
                    .from(orders)
                    .where(orders.user.no.eq(userNo))
                    .fetchOne();
    /*
      SELECT COUNT(*)
      FROM orders
      WHERE user_no = ?
    */
   return new PageImpl<>(content, pageable, total == null ? 0 : total);
  }
  
}
