package com.aloha.shop.service.users;

import java.util.List;

import com.aloha.shop.domain.users.Address;
import com.aloha.shop.dto.users.AddressDto;

public interface AddressService {

  // 회원 배송지 목록
  List<Address> list(Long userNo);

  // 배송지 등록
  Address add(Long userNo, AddressDto dto);
  
  // 배송지 수정
  Address update(Long userNo, AddressDto dto);
  
  // 배송지 삭제
  void delete(Long userNo, Long addressNo);
  
}
