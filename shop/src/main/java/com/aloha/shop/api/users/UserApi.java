package com.aloha.shop.api.users;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aloha.shop.service.users.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApi {

  private final UserService userService;

  // 아이디 중복 확인
  @GetMapping("/check-username")
  public ResponseEntity<?> checkUsername(
    @RequestParam("username") String username
  ) {
    try {
      boolean available = userService.isUsernameAvailable(username);
      return new ResponseEntity<>(available, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  
}