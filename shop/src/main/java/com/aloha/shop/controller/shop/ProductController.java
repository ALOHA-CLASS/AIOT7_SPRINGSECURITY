package com.aloha.shop.controller.shop;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aloha.shop.domain.shop.Product;
import com.aloha.shop.service.shop.ProductService;

import lombok.RequiredArgsConstructor;


/**
 * 상품
 */
@Controller 
@RequestMapping("/products")
@RequiredArgsConstructor 
public class ProductController {

  private final ProductService productService;

  // 상품 목록 화면
  @GetMapping()
  public String list(Model model) {
    List<Product> products = productService.list();
    model.addAttribute("products", products);
    return "page/products/list";
  }
  
}



