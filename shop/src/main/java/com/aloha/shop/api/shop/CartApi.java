package com.aloha.shop.api.shop;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aloha.shop.domain.shop.CartItem;
import com.aloha.shop.domain.users.User;
import com.aloha.shop.security.CustomUser;
import com.aloha.shop.service.shop.CartItemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@RestController 
@RequestMapping("/api/cart")
@RequiredArgsConstructor 
public class CartApi {

  private final CartItemService cartItemService;

  // sp-crud
  @GetMapping()
  public ResponseEntity<?> getAll(
    @AuthenticationPrincipal CustomUser loginUser
  ) {
      try {
          // 로그인된 사용자 정보의 no 가져오기
          Long userNo = loginUser.getUser().getNo();
          List<CartItem> list = cartItemService.listAll(userNo);
          return new ResponseEntity<>(list, HttpStatus.OK);
      } catch (Exception e) {
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<?> getOne(@PathVariable("id") String id) {
      try {
          CartItem cartItem = cartItemService.select(id);
          return new ResponseEntity<>(cartItem, HttpStatus.OK);
      } catch (Exception e) {
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }
  
  @PostMapping()
  public ResponseEntity<?> create(
    @RequestBody CartItem cartItem,
    @AuthenticationPrincipal CustomUser loginUser
  ) {
      try {
          // 로그인된 사용자 정보의 no 가져오기
          Long userNo = loginUser.getUser().getNo();
          if( cartItem.getUser() == null || cartItem.getUser().getNo() == null ) {
            User user = new User();
            user.setNo(userNo);
            cartItem.setUser(user);
          }
          log.info("productNo : {}", cartItem.getProduct().getNo());
          log.info("userNo : {}", cartItem.getUser().getNo());
          CartItem newCartItem = cartItemService.create(cartItem);
          return new ResponseEntity<>(newCartItem, HttpStatus.OK);
      } catch (Exception e) {
          e.printStackTrace();
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }
  
  @PutMapping()
  public ResponseEntity<?> update(@RequestBody CartItem cartItem) {
      try {
          CartItem updatedCartItem = cartItemService.update(cartItem);
          return new ResponseEntity<>(updatedCartItem, HttpStatus.OK);
      } catch (Exception e) {
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }
  
  @DeleteMapping("/{id}")
  public ResponseEntity<?> destroy(@PathVariable("id") String id) {
      try {
          cartItemService.delete(id);
          return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
      } catch (Exception e) {
        e.printStackTrace();
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }

  
}
