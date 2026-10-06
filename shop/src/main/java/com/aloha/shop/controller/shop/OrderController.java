package com.aloha.shop.controller.shop;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import com.aloha.shop.domain.shop.CartItem;
import com.aloha.shop.domain.shop.Orders;
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

  // 토스페이먼츠 결제창형 결제 연동 키 (클라이언트 키만 화면에 노출됨)
  @Value("${tosspayments.client-key}")
  private String tossClientKey;

  // 회원번호 기반의 구매자 식별키 생성 (유추 불가능한 값으로 변환)
  private String generateCustomerKey(Long userNo) {
    return UUID.nameUUIDFromBytes(("user-" + userNo).getBytes(StandardCharsets.UTF_8)).toString();
  }

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
    model.addAttribute("tossClientKey", tossClientKey);
    model.addAttribute("customerKey", generateCustomerKey(userNo));

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
    model.addAttribute("tossClientKey", tossClientKey);
    model.addAttribute("customerKey", generateCustomerKey(userNo));

    return "page/orders/checkout";
  }

  // 주문 생성 (결제창 호출 직전 AJAX 요청)
  @PostMapping
  @ResponseBody
  public Map<String, Object> order(
    @AuthenticationPrincipal CustomUser loginUser,
    @RequestBody OrderForm orderForm
  ) {
    orderForm.setUserNo(loginUser.getUser().getNo());
    Orders order = orderService.order(orderForm);     // 주문 생성

    // 주문이름 생성 :  예) 백팩 외 3건
    String firstProductName = order.getOrderItems().get(0).getProductName();
    int itemCount = order.getOrderItems().size();
    String orderName = itemCount > 1 ? firstProductName + " 외 " + (itemCount - 1) + "건" : firstProductName;

    Map<String, Object> result = new HashMap<>();
    result.put("orderNo", order.getOrderNo());
    result.put("orderName", orderName);
    result.put("totalAmount", order.getTotalAmount());
    result.put("customerName", loginUser.getUser().getName());
    result.put("customerEmail", loginUser.getUser().getEmail() == null ? "" : loginUser.getUser().getEmail());
    return result;
  }

  // 결제 성공 리다이렉트 (토스페이먼츠 successUrl)
  @GetMapping("/success")
  public String success(
    @RequestParam("orderId") String orderId,
    @RequestParam("paymentKey") String paymentKey,
    @RequestParam("amount") int amount,
    Model model
  ) {
    try {
      // 결제 승인 요청
      Orders order = orderService.confirmPayment(orderId, paymentKey, amount);
      model.addAttribute("order", order);
      return "page/orders/success";     // 주문 성공 화면
    } catch (Exception e) {
      log.error("결제 승인 실패", e);
      model.addAttribute("orderId", orderId);
      model.addAttribute("message", e.getMessage());
      return "page/orders/fail";        // 주문 실패 화면
    }
  }

  // 결제 실패 리다이렉트 (토스페이먼츠 failUrl)
  @GetMapping("/fail")
  public String fail(
    @RequestParam(name = "code", required = false) String code,
    @RequestParam(name = "message", required = false) String message,
    @RequestParam(name = "orderId", required = false) String orderId,
    Model model
  ) {
    // 결제 실패 시 생성해둔 주문을 취소하고 재고를 복원
    orderService.failPayment(orderId);

    model.addAttribute("code", code);
    model.addAttribute("message", message);
    model.addAttribute("orderId", orderId);
    return "page/orders/fail";
  }


  // 주문 내역
  @GetMapping
  public String list(
    @AuthenticationPrincipal CustomUser loginUser,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "5") int size,
    @RequestParam(value = "count", defaultValue = "10") int count,
    Model model
  ) {
    Long userNo = loginUser.getUser().getNo();
    Pageable pageable = PageRequest.of(page, size);
    Page<Orders> orders = orderService.list(userNo, pageable);
    model.addAttribute("orders", orders);
    model.addAttribute("count", count);

    // 페이지 URL 생성
    // - /orders?size=8&count=10
    String url = UriComponentsBuilder
                  .fromPath("/orders")
                  .queryParam("size", size)
                  .queryParam("count", count)
                  .toUriString();
    log.info("url : {}", url);
    model.addAttribute("url", url);
    return "page/orders/list";
  }
  
  // 주문 상세
  @GetMapping("/{no}")
  public String detail(
    @AuthenticationPrincipal CustomUser loginUser,
    @PathVariable("no") Long no,
    Model model
  ) {
    Long userNo = loginUser.getUser().getNo();
    Orders order = orderService.select(userNo, no);
    model.addAttribute("order", order);
    return "page/orders/detail";
  }
  

}

