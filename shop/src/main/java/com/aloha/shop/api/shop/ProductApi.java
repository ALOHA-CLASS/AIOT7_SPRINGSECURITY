package com.aloha.shop.api.shop;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aloha.shop.domain.shop.Product;
import com.aloha.shop.service.shop.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@RestController 
@RequestMapping("/api/products")
@RequiredArgsConstructor 
public class ProductApi {

  private final ProductService productService;

  // sp-crud
  @GetMapping()
  public ResponseEntity<?> getAll() {
      try {
          List<Product> list = productService.list();
          return new ResponseEntity<>(list, HttpStatus.OK);
      } catch (Exception e) {
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<?> getOne(@PathVariable("id") String id) {
      try {
          Product product = productService.select(id);
          return new ResponseEntity<>(product, HttpStatus.OK);
      } catch (Exception e) {
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }
  
  @PostMapping()
  public ResponseEntity<?> create(@RequestBody Product product) {
      try {
          Product newProdcut = productService.create(product);
          return new ResponseEntity<>(newProdcut, HttpStatus.CREATED);
      } catch (Exception e) {
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }
  
  @PutMapping()
  public ResponseEntity<?> update(@RequestBody Product product) {
      try {
          Product updatedProduct = productService.update(product);
          return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
      } catch (Exception e) {
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }
  
  @DeleteMapping("/{id}")
  public ResponseEntity<?> destroy(@PathVariable("id") String id) {
      try {
          log.info("id : {}", id);
          productService.delete(id);
          return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
      } catch (Exception e) {
          log.error("error : {}", e);
          e.printStackTrace();
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }
  
}
