package com.aloha.shop.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.aloha.shop.domain.shop.Product;
import com.aloha.shop.service.shop.ProductService;

import lombok.RequiredArgsConstructor;


@Controller 
@RequiredArgsConstructor 
public class HomeController {

  private final ProductService productService;

  @GetMapping("/")
  public String home(Model model) {
    // 최신 상품 8개 
    Pageable pageable = PageRequest.of(0, 8);
    Page<Product> products = productService.list(pageable);
    model.addAttribute("products", products);
    return "index";
  }
  
  
}
