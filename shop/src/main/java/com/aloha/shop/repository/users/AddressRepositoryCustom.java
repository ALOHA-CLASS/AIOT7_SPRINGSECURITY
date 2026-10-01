package com.aloha.shop.repository.users;

import com.aloha.shop.domain.users.Address;

public interface AddressRepositoryCustom {

  /**
   * 배송지 ID로 조회
   * SELECT *
   * FROM address
   * WHERE id = ?
   * @param id
   * @return
   */
  Address findById(String id);
  
}
