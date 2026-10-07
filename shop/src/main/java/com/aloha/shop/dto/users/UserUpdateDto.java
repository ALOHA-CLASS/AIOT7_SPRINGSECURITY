package com.aloha.shop.dto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// 회원정보 수정 폼 DTO
@Data 
public class UserUpdateDto {

  @NotBlank(message = "이름을 입력하세요.")
  private String name;
  
  @Email(message = "이메일 형식이 올바르지 않습니다.")
  private String email;
  
  private String phone;

  private String password;
  
}
