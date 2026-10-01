package com.aloha.shop.api.users;

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

import com.aloha.shop.domain.users.Address;
import com.aloha.shop.dto.users.AddressDto;
import com.aloha.shop.security.CustomUser;
import com.aloha.shop.service.users.AddressService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@RestController 
@RequestMapping("/api/address")
@RequiredArgsConstructor 
public class AddressApi {

  private final AddressService addressService;

  // sp-crud
  @GetMapping()
  public ResponseEntity<?> getAll(
    @AuthenticationPrincipal CustomUser loginUser
  ) {
      try {
        Long userNo = loginUser.getUser().getNo();
        List<Address> list = addressService.list(userNo);
        return new ResponseEntity<>(list, HttpStatus.OK);
      } catch (Exception e) {
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<?> getOne(
    @PathVariable String id,
    @AuthenticationPrincipal CustomUser loginUser
  ) {
      try {
          Long userNo = loginUser.getUser().getNo();
          Address address = addressService.select(userNo, id);
          return new ResponseEntity<>(address, HttpStatus.OK);
      } catch (Exception e) {
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }
  
  @PostMapping()
  public ResponseEntity<?> create(
    @Valid @RequestBody AddressDto addressDto,
    @AuthenticationPrincipal CustomUser loginUser
  ) {
      try {
        Long userNo = loginUser.getUser().getNo();
        Address newAddress = addressService.add(userNo, addressDto);
        return new ResponseEntity<>(newAddress, HttpStatus.OK);
      } catch (Exception e) {
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }
  
  @PutMapping()
  public ResponseEntity<?> update(
    @Valid @RequestBody AddressDto addressDto,
    @AuthenticationPrincipal CustomUser loginUser
  ) {
      try {
          Long userNo = loginUser.getUser().getNo();
          Address updatedAddress = addressService.update(userNo, addressDto);
          return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
      } catch (Exception e) {
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }
  
  @DeleteMapping("/{id}")
  public ResponseEntity<?> destroy(
    @PathVariable("id") String id,
    @AuthenticationPrincipal CustomUser loginUser
  ) {
      try {
          Long userNo = loginUser.getUser().getNo();
          Address address = addressService.select(userNo, id);
          Long addressNo = address.getNo();
          addressService.delete(userNo, addressNo);
          return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
      } catch (Exception e) {
          e.printStackTrace();
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }
  
}
