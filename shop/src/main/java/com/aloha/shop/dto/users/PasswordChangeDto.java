package com.aloha.shop.dto.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// 비밀번호 변경 폼
@Data 
public class PasswordChangeDto {
  
  @NotBlank(message = "새 비밀번호를 입력하세요.")
  @Size(min = 6, message = "비밀번호는 6자 이상 입력하세요.")
  private String newPassword;

  @NotBlank(message = "새 비밀번호 확인을 입력하세요.")
  private String newPasswordConfirm;
  
}
