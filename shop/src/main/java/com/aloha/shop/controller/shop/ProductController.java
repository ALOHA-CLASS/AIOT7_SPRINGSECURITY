package com.aloha.shop.controller.shop;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.aloha.shop.domain.shop.Product;
import com.aloha.shop.dto.shop.ProductSearch;
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
                     @RequestParam(name = "count", defaultValue = "10") int count,
                     @ModelAttribute ProductSearch search
  ) {
    // 전체 목록
    // List<Product> products = productService.list();
    
    // ✨ 페이징 목록
    Pageable pageable = PageRequest.of(page, size);
    // Page<Product> products = productService.list(pageable);
    Page<Product> products = productService.list(pageable, search);     // ⭐ 필터&검색
    log.info("##### [페이징] 상품 목록 #####");
    log.info("- 현재 페이지 : " + products.getNumber());
    log.info("- 페이지당 개수 : " + products.getSize());
    log.info("- 총 페이지 수 : " + products.getTotalPages());
    log.info("-->" + products.getContent());

    model.addAttribute("products", products);
    model.addAttribute("count", count);           // 노출 페이지 수 (1~10)

    // 페이지 URL 생성
    // - /products?size=8&count=10&category=의류&keyword=가방
    String url = UriComponentsBuilder
                  .fromPath("/products")
                  .queryParam("size", size)
                  .queryParam("count", count)
                  .queryParam("category", search.getCategory())
                  .queryParam("keyword", search.getKeyword())
                  .toUriString();
    log.info("url : {}", url);
    model.addAttribute("url", url);

    return "page/products/list";
  }
  

  // 상품 상세 화면
  @GetMapping("/{id}")
  public String detail(@PathVariable("id") String id, Model model) {
    // 상품 id 로 조회 요청
    Product product = productService.select(id);

    // 모델 등록
    model.addAttribute("product", product);

    // 뷰 지정
    return "page/products/detail";
  }


  // AJAX 테스트 화면
  @GetMapping("/test")
  public String test() {
    return "page/products/test";
  }
  
  
}



