package com.aloha.shop.repository.shop;

import org.springframework.stereotype.Repository;

import com.aloha.shop.domain.shop.CartItem;
import com.aloha.shop.domain.shop.QCartItem;
import com.querydsl.jpa.JPQLQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository 
@RequiredArgsConstructor 
public class CartItemRepositoryImpl implements CartItemRepositoryCustom {
  
  private final JPQLQueryFactory queryFactory;


  @Override
  public CartItem findById(String id) {
    QCartItem cartItem = QCartItem.cartItem;
    CartItem result = queryFactory
                        .selectFrom(cartItem)
                        .where(cartItem.id.eq(id))
                        .fetchOne();
    return result;
  }

  @Override
  public void deleteById(String id) {
    QCartItem cartItem = QCartItem.cartItem;

    queryFactory.delete(cartItem)
                .where(cartItem.id.eq(id))
                .execute();
    
  }

  

  
}
