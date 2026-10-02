package com.aloha.shop.controller.shop;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aloha.shop.domain.shop.CartItem;
import com.aloha.shop.domain.shop.Product;
import com.aloha.shop.domain.users.Address;
import com.aloha.shop.dto.shop.OrderForm;
import com.aloha.shop.security.CustomUser;
import com.aloha.shop.service.shop.CartItemService;
import com.aloha.shop.service.shop.OrderService;
import com.aloha.shop.service.shop.ProductService;
import com.aloha.shop.service.users.AddressService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Controller 
@RequestMapping("/orders")
@RequiredArgsConstructor 
public class OrderController {

  private final OrderService orderService;
  private final CartItemService cartItemService;
  private final ProductService productService;
  private final AddressService addressService;

  // 주문서 작성 화면 (상품상세)
  @GetMapping("/checkout/direct")
  public String checkOutDirect(
    @AuthenticationPrincipal CustomUser loginUser,
    @RequestParam("productNo") Long productNo,
    @RequestParam(name = "quantity", defaultValue = "1") int quantity,
    Model model
  ) {
    Long userNo = loginUser.getUser().getNo();
    // 상품 1건 조회
    Product product = productService.select(productNo);
    OrderForm orderForm = new OrderForm();
    orderForm.setQuantity(quantity);

    // 배송지 목록
    List<Address> addressList = addressService.list(userNo);

    // 모델에 등록
    model.addAttribute("directProduct", product);
    model.addAttribute("directQuantity", quantity);
    model.addAttribute("totalPrice", product.getPrice() * quantity);
    model.addAttribute("addressList", addressList);
    model.addAttribute("orderForm", orderForm);

    return "page/orders/checkout";
  }

  // 주문서 작성 화면 (장바구니)
  @GetMapping("/checkout")
  public String checkout(
    @AuthenticationPrincipal CustomUser loginUser,
    Model model
  ) {
    Long userNo = loginUser.getUser().getNo();
    // 장바구니가 비었으면 다시 장바구니 내역으로 돌아가기
    Long cartCount = cartItemService.count(userNo);
    if( cartCount == 0 ) {
      return "redirect:/cart";
    }

    // 장바구니 목록, 배송지 목록, 총 주문금액 
    List<CartItem> items = cartItemService.listAll(userNo);
    List<Address> addressList = addressService.list(userNo);
    int totalPrice = cartItemService.totalPrice(userNo);

    // 모델에 등록
    model.addAttribute("items", items);
    model.addAttribute("addressList", addressList);
    model.addAttribute("totalPrice", totalPrice);
    model.addAttribute("orderForm", new OrderForm());

    return "page/orders/checkout";
  }


}
