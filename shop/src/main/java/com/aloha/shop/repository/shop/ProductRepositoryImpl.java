package com.aloha.shop.repository.shop;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.aloha.shop.domain.shop.Product;
import com.aloha.shop.domain.shop.QProduct;
import com.aloha.shop.dto.shop.ProductSearch;
import com.querydsl.core.BooleanBuilder;
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

  @Override
  public Page<Product> page(Pageable pageable, ProductSearch search) {
    QProduct product = QProduct.product;

    // 동적 쿼리 조건
    BooleanBuilder where = new BooleanBuilder();
    if( search != null ) {
      if( StringUtils.hasText(search.getCategory()) ) {
        // 상품 카테고리가 지정된 경우
        // WHERE category = ?
        where.and(product.category.eq(search.getCategory()));
      }

      if( StringUtils.hasText(search.getKeyword()) ) {
        // 검색어가 포함되는 상품명을 조회
        // WHERE name LIKE %keyword% OR description LIKE %keyword%
        where.and(
                  product.name.containsIgnoreCase(search.getKeyword())
                  .or(product.description.containsIgnoreCase(search.getKeyword()))
                );
      }
    }


    // 페이징 처리 목록 쿼리
    List<Product> content = queryFactory
                            .selectFrom(product)
                            .where(where)               // 필터&검색 조건 ⭐
                            .orderBy(product.no.desc())
                            .offset(pageable.getOffset())
                            .limit(pageable.getPageSize())
                            .fetch();

    // 전체 데이터 개수
    Long total = queryFactory
                    .select(product.count())
                    .from(product)
                    .where(where)                       // 필터&검색 조건 ⭐
                    .fetchOne();

    // Page 객체 반환
    Page<Product> page = new PageImpl<>(content, pageable, total == null ? 0 : total);
    return page;
  }

  @Override
  public Product findById(String id) {
    QProduct product = QProduct.product;
    Product result = queryFactory
                        .selectFrom(product)
                        .where(product.id.eq(id))
                        .fetchOne();
    return result;
  }

  @Override
  public void deleteById(String id) {
    QProduct product = QProduct.product;

    queryFactory.delete(product)
                .where(product.id.eq(id))
                .execute();
    
  }
  
}
