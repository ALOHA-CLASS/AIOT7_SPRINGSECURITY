package com.aloha.shop.dto.users;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data 
public class AddressDto {

  private Long no;

  @NotBlank(message = "배송지명을 입력하세요.")
  private String name;

  @NotBlank(message = "받는 분을 입력하세요.")
  private String receiver;
  
  private String phone;
  private String zipcode;
  
  @NotBlank(message = "기본주소를 입력하세요.")
  private String address1;
  private String address2;

  private boolean defaultAddress;
  
}
