package com.aloha.shop.repository.shop;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.aloha.shop.domain.shop.Product;
import com.aloha.shop.domain.shop.QProduct;
import com.querydsl.jpa.JPQLQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository 
@RequiredArgsConstructor 
public class ProductRepositoryImpl implements ProductRepositoryCustom {

  private final JPQLQueryFactory queryFactory;

  @Override
  public Page<Product> page(Pageable pageable) {
    QProduct product = QProduct.product;

    // 페이징 처리 목록 쿼리
    List<Product> content = queryFactory
                            .selectFrom(product)
                            .orderBy(product.no.desc())
                            .offset(pageable.getOffset())
                            .limit(pageable.getPageSize())
                            .fetch();

    // 전체 데이터 개수
    Long total = queryFactory
                    .select(product.count())
                    .from(product)
                    .fetchOne();

    // Page 객체 반환
    Page<Product> page = new PageImpl<>(content, pageable, total == null ? 0 : total);
    return page;
  }
  
}
