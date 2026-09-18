package com.aloha.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aloha.security.domain.Users;
import com.aloha.security.dto.CustomUser;
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
   * @throws Exception 
   */
  @GetMapping("")
  // public String home(@AuthenticationPrincipal User authUser, Model model) throws Exception {
  public String home(@AuthenticationPrincipal CustomUser customUser, Model model) throws Exception {

    // @AuthenticationPrincipal User authUser : 인증된 사용자 객체
    // if( authUser != null ) {
    //   log.info("user : {}", authUser);
    //   String username = authUser.getUsername();     // 아이디
    //   Users user = userService.select(username);    // 회원 정보 조회
    //   model.addAttribute("user", user);
    // }

    if( customUser != null ) {
      log.info("customUser : {}", customUser);
      Users user = customUser.getUser();
      model.addAttribute("user", user);
    }

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

    // 암호화 전 비밀번호
    String plainPassword = user.getPassword();
    // 회원 가입 처리
    boolean result = userService.join(user);

    // 회원 가입 성공
    if( result ) {
      // 암호화 전 비밀번호 다시 세팅
      user.setPassword(plainPassword);
      boolean loginResult = userService.login(user, request);
      if( loginResult )
        return "redirect:/";          // 바로 로그인 ➡ 메인 화면
      else 
        return "redirect:/login";     // 바로 로그인 실패 ➡ 로그인 화면
    }

    // 회원 가입 실패
    return "redirect:/join?error";
  }
  

  /**
   * 아이디 중복 검사
   * @param username
   * @return
   * @throws Exception
   */
  @ResponseBody 
  @GetMapping("/check/{username}")
  public ResponseEntity<Boolean> userCheck(
    @PathVariable("username") String username
  ) throws Exception {
    log.info("아이디 중복 확인 : {}", username);
    Users user = userService.select(username);
    
    // 아이디 중복
    if( user != null ) {
      log.info("중복된 아이디 입니다 - {}", username);
      return new ResponseEntity<>(false, HttpStatus.OK);
    }
    // 사용 가능한 아이디입니다.
    log.info("사용 가능한 아이디입니다. - {}", username);
    return new ResponseEntity<>(true, HttpStatus.OK);
  }
  
  /**
   * 로그인 화면
   * 🔗 [GET] - /login
   * @return
   */
  @GetMapping("/login")
  public String login() {
    log.info("::::: 로그인 화면 :::::");
    

    return "login";
  }  
  
}
