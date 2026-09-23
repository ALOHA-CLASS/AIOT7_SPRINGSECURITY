package com.aloha.shop.controller.shop;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aloha.shop.domain.shop.Product;
import com.aloha.shop.service.shop.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 상품 컨트롤러
 */
@Slf4j 
@Controller 
@RequestMapping("/products")
@RequiredArgsConstructor 
public class ProductController {

  private final ProductService productService;

  // 상품 목록 화면
  @GetMapping()
  public String list(Model model,
                     @RequestParam(name = "page", defaultValue = "0") int page,
                     @RequestParam(name = "size", defaultValue = "8") int size,
                     @RequestParam(name = "count", defaultValue = "10") int count
  ) {
    // 전체 목록
    // List<Product> products = productService.list();
    
    // ✨ 페이징 목록
    Pageable pageable = PageRequest.of(page, size);
    Page<Product> products = productService.list(pageable);
    log.info("##### [페이징] 상품 목록 #####");
    log.info("- 현재 페이지 : " + products.getNumber());
    log.info("- 페이지당 개수 : " + products.getSize());
    log.info("- 총 페이지 수 : " + products.getTotalPages());
    log.info("-->" + products.getContent());

    model.addAttribute("products", products);
    model.addAttribute("count", count);           // 노출 페이지 수 (1~10)
    return "page/products/list";
  }
  
}



