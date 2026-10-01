package com.aloha.shop.service.users;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aloha.shop.domain.users.Address;
import com.aloha.shop.domain.users.User;
import com.aloha.shop.dto.users.AddressDto;
import com.aloha.shop.repository.users.AddressRepository;
import com.aloha.shop.repository.users.UserRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class AddressServiceImpl implements AddressService {

  private final AddressRepository addressRepository;
  private final UserRepository userRepository;

  @Override
  public List<Address> list(Long userNo) {
    return addressRepository.findByUserNoOrderByDefaultAddressDescNoDesc(userNo);
  }

  @Override
  @Transactional 
  public Address add(Long userNo, AddressDto dto) {
    User user = userRepository.findById(userNo)
              .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    
    // 기본 배송지 해제
    if( dto.isDefaultAddress() ) {
      clearDefault(userNo);
    }

    Address address = Address.builder()
                             .user(user)
                             .receiver(dto.getReceiver())
                             .zipcode(dto.getZipcode())
                             .address1(dto.getAddress1())
                             .address2(dto.getAddress2())
                             .defaultAddress(dto.isDefaultAddress())
                             .build();
    return addressRepository.save(address);
  }

  @Override
  @Transactional
  public Address update(Long userNo, AddressDto dto) {
    // 배송지 소유자 확인
    Address address = getOwned(userNo, dto.getNo());

    // 기본 배송지 해제
    if( dto.isDefaultAddress() ) {
      clearDefault(userNo);
    }

    address.setReceiver(dto.getReceiver());
    address.setPhone(dto.getPhone());
    address.setZipcode(dto.getZipcode());
    address.setAddress1(dto.getAddress1());
    address.setAddress2(dto.getAddress2());
    address.setDefaultAddress(dto.isDefaultAddress());
    
    return addressRepository.save(address);
  }

  @Override
  @Transactional
  public void delete(Long userNo, Long addressNo) {
    // 배송지 소유자 확인
    Address address = getOwned(userNo, addressNo);

    addressRepository.delete(address);
  }


  // 배송지 소유자 여부 확인
  private Address getOwned(Long userNo, Long addressNo) {
    Address address = addressRepository.findById(addressNo)
              .orElseThrow(() -> new IllegalArgumentException("배송지를 찾을 수 없습니다."));

    // 배송지 소유자 인지
    // - 배송지의 userNo 랑 요청한 회원 no 랑 일치하지 않으면 소유자가 아님
    boolean check = !address.getUser().getName().equals(userNo);
    if( check ) {
      throw new IllegalArgumentException("본인의 배송지가 아닙니다.");
    }
    return address;
  }

  /**
   * 기본 배송지 해제
   * ✅ 배송지1
   *     배송지2
   *     배송지3
   * 배송지2를 기본 배송지로 하려고 한다면,
   * 기본 배송지인 배송지1을 기본 배송지에서 해제한다.
   * @param userNo
   */
  private void clearDefault(Long userNo) {
    List<Address> addressList = addressRepository.findByUserNoOrderByDefaultAddressDescNoDesc(userNo);
    for (Address address : addressList) {
      // 기본 배송지이면, 해제
      if(address.isDefaultAddress()) {
        address.setDefaultAddress(false);   // 기본 배송지 해제❌
        addressRepository.save(address);
      }
    }
  }
  
}
