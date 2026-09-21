package com.aloha.layout.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller 
public class MainController {

  // 메인 화면
  @GetMapping("/main")
  public String main() {
    return "main";
  }
  
}
