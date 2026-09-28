package com.aloha.shop.controller.shop;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.aloha.shop.domain.shop.CartItem;
import com.aloha.shop.security.CustomUser;
import com.aloha.shop.service.shop.CartItemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j 
@Controller 
@RequestMapping("/cart")
@RequiredArgsConstructor 
public class CartController {

  private final CartItemService cartItemService;

  /**
   * 장바구니 내역
   * @param param
   * @return
   */
  @GetMapping("")
  public String list(
    @AuthenticationPrincipal CustomUser loginUser,
    @RequestParam(name = "page", defaultValue = "0") int page,
    @RequestParam(name = "size", defaultValue = "5") int size,
    @RequestParam(name = "count", defaultValue = "10") int count,
    Model model
  ) {
    // 로그인된 사용자 정보의 no 가져오기
    Long userNo = loginUser.getUser().getNo();

    // 장바구니 내역 조회
    Page<CartItem> items = cartItemService.list(userNo, PageRequest.of(page, size));

    // 총 주문 금액
    int totalPrice = cartItemService.totalPrice(userNo);

    // 모델 등록
    model.addAttribute("items", items);
    model.addAttribute("count", count);
    model.addAttribute("totalPrice", totalPrice);

    // 페이지 URL 생성
    // - /cart?size=5&count=10
    String url = UriComponentsBuilder
                  .fromPath("/cart")
                  .queryParam("size", size)
                  .queryParam("count", count)
                  .toUriString();
    log.info("url : {}", url);
    model.addAttribute("url", url);
    
    // 뷰 지정
    return "page/cart/list";
  }
  
  
}