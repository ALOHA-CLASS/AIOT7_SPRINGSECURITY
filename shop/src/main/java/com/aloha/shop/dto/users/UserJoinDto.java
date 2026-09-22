package com.aloha.shop.dto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// 회원가입 폼 DTO
@Data 
public class UserJoinDto {

  @NotBlank(message = "아이디를 입력하세요.")
  @Size(min = 4, max = 50, message = "아이디는 4~50자로 입력하세요.")
  private String username;

  @NotBlank(message = "비밀번호를 입력하세요.")
  @Size(min = 6, message = "비밀번호는 6자 이상 입력하세요.")
  private String password;

  @NotBlank(message = "이름을 입력하세요.")
  private String name;

  @Email(message = "이메일 형식이 올바르지 않습니다.")
  private String email;
  
  private String phone;
  
}
