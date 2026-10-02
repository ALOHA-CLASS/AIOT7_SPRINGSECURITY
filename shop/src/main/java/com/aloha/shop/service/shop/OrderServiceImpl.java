package com.aloha.shop.service.shop;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpStatusCodeException;

import com.aloha.shop.domain.shop.CartItem;
import com.aloha.shop.domain.shop.OrderItem;
import com.aloha.shop.domain.shop.OrderStatus;
import com.aloha.shop.domain.shop.Orders;
import com.aloha.shop.domain.shop.Product;
import com.aloha.shop.domain.users.User;
import com.aloha.shop.dto.shop.OrderForm;
import com.aloha.shop.repository.shop.CartItemRepository;
import com.aloha.shop.repository.shop.OrderRepository;
import com.aloha.shop.repository.shop.ProductRepository;
import com.aloha.shop.repository.users.UserRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final CartItemRepository cartItemRepository;
  private final RestClient restClient = RestClient.create();      // 다른서버로 요청보내는 객체

  @Value("${tosspayments.secret-key}")                            // application.properties 에서 가져옴
  private String tossSecretKey;

  private static final String TOSS_CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";

  @Override
  @Transactional 
  public Orders orderDirect(OrderForm orderForm) {
    int quantity = orderForm.getQuantity() == null ? 1 : orderForm.getQuantity();
    if( quantity < 1 ) quantity = 1;

    // 회원
    Long userNo = orderForm.getUserNo();
    User user = userRepository.findById(userNo)
              .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    // 상품
    Product product = productRepository.findById(orderForm.getProductNo())
              .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

    // 상품 재고 확인
    if( product.getStock() < quantity ) {
      throw new IllegalStateException("재고가 부족합니다: " + product.getName());
    }
    // 주문 수량만큼 재고 감소
    product.setStock(product.getStock() - quantity);

    // 주문 등록
    Orders order = Orders.builder()
                         .user(user)
                         .status(OrderStatus.ORDERED)
                         .orderNo(Orders.generateOrderNo())
                         .receiver(orderForm.getReceiver())
                         .phone(orderForm.getPhone())
                         .zipcode(orderForm.getZipcode())
                         .address1(orderForm.getAddress1())
                         .address2(orderForm.getAddress2())
                         .build();
    OrderItem orderItem = OrderItem.builder()
                                   .product(product)
                                   .productName(product.getName())
                                   .price(product.getPrice())
                                   .quantity(quantity)
                                   .build();
    order.addOrderItem(orderItem);                  // 주문서에 주문항목1건 지정
    order.setTotalAmount(orderItem.getSubtotal());  // 주문 총금액 계산

    return orderRepository.save(order);
  }

  @Override
  @Transactional 
  public Orders orderCart(OrderForm orderForm) {

    // 회원
    Long userNo = orderForm.getUserNo();
    User user = userRepository.findById(userNo)
              .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

    // 회원의 장바구니 목록 조회
    List<CartItem> cartItems = cartItemRepository.findByUserNo(userNo);
    if(cartItems.isEmpty()) {
      throw new IllegalStateException("장바구니가 비어 있습니다.");
    }

    // 주문 객체
    Orders order = Orders.builder()
                         .user(user)
                         .status(OrderStatus.ORDERED)
                         .orderNo(Orders.generateOrderNo())
                         .receiver(orderForm.getReceiver())
                         .phone(orderForm.getPhone())
                         .zipcode(orderForm.getZipcode())
                         .address1(orderForm.getAddress1())
                         .address2(orderForm.getAddress2())
                         .build();

    // 장바구니 항목을 반복해서 주문항목을 생성
    int total = 0;
    for (CartItem cart : cartItems) {
      Product product = cart.getProduct();

      // 상품 재고 확인
      if( product.getStock() < cart.getQuantity() ) {
        throw new IllegalStateException("재고가 부족합니다: " + product.getName());
      }
      // 주문 수량만큼 재고 감소
      product.setStock(product.getStock() - cart.getQuantity());

      // 장바구니 항목 ➡ 주문 항목
      OrderItem orderItem = OrderItem.builder()
                                     .product(product)
                                     .productName(product.getName())
                                     .price(product.getPrice())
                                     .quantity(cart.getQuantity())
                                     .build();
      order.addOrderItem(orderItem);                  // 주문서에 주문항목 추가
      // 총 주문금액 합계 구하기
      total += orderItem.getSubtotal();
    }
    order.setTotalAmount(total);  // 주문 총금액 계산

    // 주문 등록
    Orders saved = orderRepository.save(order);

    // 주문 완료 후 장바구니 비우기
    cartItemRepository.deleteByUserNo(userNo);

    return saved;



  }

  @Override
  @Transactional
  public Orders order(OrderForm orderForm) {
    // 상품번호가 있으면 바로구매, 없으면 장바구니 전체주문
    if (orderForm.getProductNo() != null) {
      return orderDirect(orderForm);    // 상품상세 -> 바로주문
    }
    return orderCart(orderForm);        // 장바구니 -> 전체주문
  }

  @Override
  @Transactional
  public Orders confirmPayment(String orderNo, String paymentKey, int amount) {
    Orders order = orderRepository.findByOrderNo(orderNo)
                  .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

    // 이미 승인된 주문이면 중복 승인 방지
    if (order.getStatus() == OrderStatus.PAID) {
      return order;
    }

    // 결제 금액 위변조 검증
    if (order.getTotalAmount() != amount) {
      cancelInternal(order);
      throw new IllegalStateException("결제 금액이 일치하지 않습니다.");
    }

    // 토스페이먼츠 결제 승인 API 호출
    String encodedKey = Base64.getEncoder()
                              .encodeToString((tossSecretKey + ":").getBytes(StandardCharsets.UTF_8));
    Map<String, Object> body = Map.of(
        "paymentKey", paymentKey,
        "orderId", orderNo,
        "amount", amount
    );

    try {
      // toss 로 결제 승인 요청
      restClient.post()
                .uri(TOSS_CONFIRM_URL)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + encodedKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    } catch (HttpStatusCodeException e) {
      // 승인 실패 ➡ 주문 취소 및 재고 복원 후 예외 전달
      cancelInternal(order);
      throw new IllegalStateException("결제 승인에 실패했습니다: " + e.getResponseBodyAsString(), e);
    }

    // 결제 완료 처리
    order.setStatus(OrderStatus.PAID);
    order.setPaymentKey(paymentKey);
    return orderRepository.save(order);
  }

  @Override
  @Transactional
  public void failPayment(String orderNo) {
    if (orderNo == null) return;
    orderRepository.findByOrderNo(orderNo).ifPresent(this::cancelInternal);
  }

  // 주문취소 + 재고복원 공통 처리
  private void cancelInternal(Orders order) {
    if (order.getStatus() == OrderStatus.CANCELED) return;
    order.setStatus(OrderStatus.CANCELED);        
    for (OrderItem item : order.getOrderItems()) {
      Product product = item.getProduct();
      product.setStock(product.getStock() + item.getQuantity());
    }
    orderRepository.save(order);
  }

  @Override
  public Page<Orders> list(Long userNo, Pageable pageable) {
    return orderRepository.findByUser(userNo, pageable);
  }

  @Override
  public Orders select(Long userNo, Long no) {
    Orders order = orderRepository.findById(no)
                  .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
    // 본인 주문인지 확인
    if( !order.getUser().getNo().equals(userNo) ) {
      throw new IllegalCallerException("본인의 주문이 아닙니다.");
    }
    order.getOrderItems().size();   // 주문항목 지연로딩 초기화
    return order;
  }

  @Override
  @Transactional 
  public void cancel(Long userNo, Long no) {
    Orders order = select(userNo, no);

    // 배송중인거나 배송완료 주문상태에서는 주문취소 불가
    OrderStatus status = order.getStatus();
    if( status == OrderStatus.SHIPPING || status == OrderStatus.DELIVERED ) {
      throw new IllegalStateException("배송 중이거나 배송완료된 주문은 취소할 수 없습니다.");
    }

    // 주문상태를 주문취소로 수정
    order.setStatus(OrderStatus.CANCELED);

    // 재고 복원
    List<OrderItem> orderItems = order.getOrderItems();
    for (OrderItem item : orderItems) {
      Product product = item.getProduct();
      product.setStock(product.getStock() + item.getQuantity());
    }

    orderRepository.save(order);
  }
  
}
