package com.aloha.shop.service.shop;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aloha.shop.domain.shop.CartItem;
import com.aloha.shop.repository.shop.CartItemRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class CartItemServiceImpl implements CartItemService {

  private final CartItemRepository cartItemRepository;
  // private final ProductRepository productRepository;
  // private final UserRepository userRepository;

  
  @Override
  public Page<CartItem> list(Long userNo, Pageable pageable) {
    return cartItemRepository.findByUserNo(userNo, pageable);
  }
  
  @Override
  public List<CartItem> listAll(Long userNo) {
    return cartItemRepository.findByUserNo(userNo);
  }

  @Override
  public long count(Long userNo) {
    return cartItemRepository.countByUserNo(userNo);
  }

  @Override
  public int totalPrice(Long userNo) {
    // 회원의 전체 장바구니 목록 조회
    List<CartItem> list = cartItemRepository.findByUserNo(userNo);

    // (단가 x 수량)을 계산해서 합계 ➡ 총 주문 금액
    int totalPrice = list.stream()
                          .mapToInt(c -> c.getProduct().getPrice() * c.getQuantity())
                          .sum();
    return totalPrice;
  }

  @Override
  public CartItem select(String id) {
    return cartItemRepository.findById(id);
  }

  @Override
  public CartItem create(CartItem cartItem) {
    return cartItemRepository.save(cartItem);
  }

  @Override
  @Transactional 
  public CartItem update(CartItem cartItem) {
    // 장바구니 수량 변경
    CartItem old = cartItemRepository.findById(cartItem.getId());
    old.setQuantity(cartItem.getQuantity());
    return old;
  }

  @Override
  @Transactional 
  public void delete(String id) {
    cartItemRepository.deleteById(id);
  }

  


  
  
}
