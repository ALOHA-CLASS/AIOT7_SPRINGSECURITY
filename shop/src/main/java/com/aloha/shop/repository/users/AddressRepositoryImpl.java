package com.aloha.shop.repository.users;

import org.springframework.stereotype.Repository;

import com.aloha.shop.domain.users.Address;
import com.aloha.shop.domain.users.QAddress;
import com.querydsl.jpa.JPQLQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository 
@RequiredArgsConstructor 
public class AddressRepositoryImpl implements AddressRepositoryCustom {

  private final JPQLQueryFactory queryFactory;

  @Override
  public Address findById(String id) {
    QAddress address = QAddress.address;

    Address result = queryFactory
                              .selectFrom(address)
                              .where(address.id.eq(id))
                              .fetchOne();
    return result;
  }
  
}
