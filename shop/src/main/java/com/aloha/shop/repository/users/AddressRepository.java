package com.aloha.shop.repository.users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aloha.shop.domain.users.Address;

public interface AddressRepository extends JpaRepository<Address, Long>, AddressRepositoryCustom {

  /**
   * 기본 배송지 우선으로 정렬하여 회원 배송지 목록 조회
   * SELECT *
   * FROM address
   * WHERE user_no = ?
   * ORDER BY default_address DESC, no DESC
   * @param userNo
   * @return
   */
  List<Address> findByUserNoOrderByDefaultAddressDescNoDesc(Long userNo);
  
}
