package com.aloha.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.aloha.security.domain.Users;
import com.aloha.security.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j 
@Controller 
@RequiredArgsConstructor 
public class HomeController {

  private final UserService userService;

  /**
   * 메인 화면
   * 🔗 [GET] - /
   * 📜 index.html
   * @return
   */
  @GetMapping("")
  public String home() {
    // 화면 지정 : index.html
    return "index";
  }

  /**
   * 회원 가입 화면
   * 🔗 [GET] - /join
   * 📜 join.html
   * @return
   */
  @GetMapping("/join")
  public String join() {
    return "join";
  }

  /**
   * 회원 가입 처리
   * 🔗 [POST] - /join
   * ➡ ⭕ 성공 /login
   *    ❌ 실패 /join?error
   * @param user
   * @param request
   * @return
   * @throws Exception 
   */
  @PostMapping("/join")
  public String join(Users user, HttpServletRequest request) throws Exception {
    log.info("::::: 회원 가입 처리 :::::");
    log.info("user : {}", user);

    // 회원 가입 처리
    boolean result = userService.join(user);

    // 회원 가입 성공
    if( result )
      return "redirect:/login";

    // 회원 가입 실패
    return "redirect:/join?error";
  }
  
  
  
  
}
