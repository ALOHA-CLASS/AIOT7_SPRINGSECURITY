package com.aloha.shop.service.shop;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aloha.shop.domain.shop.Product;
import com.aloha.shop.dto.shop.ProductSearch;
import com.aloha.shop.repository.shop.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  @Override
  public List<Product> list() {
    return productRepository.findAll();
  }

  @Override
  public Page<Product> list(Pageable pageable) {
    return productRepository.page(pageable);
  }

  @Override
  public Product select(String id) {
    return productRepository.findById(id);
  }

  @Override
  public Page<Product> list(Pageable pageable, ProductSearch search) {
    return productRepository.page(pageable, search);
  }

  @Override
  public Product create(Product product) {
    return productRepository.save(product); // insert
  }

  @Override
  @Transactional 
  public Product update(Product product) {
    Product old = productRepository.findById(product.getId());
    old.setName(product.getName());
    old.setPrice(product.getPrice());
    old.setCategory(product.getCategory());
    old.setImageUrl(product.getImageUrl());
    old.setDescription(product.getDescription());
    old.setStock(product.getStock());
    // ⭐ @Transactional 안에서는 엔터티 변경감지를 하기 때문에
    // save() 메소드 호출하지 않아도 자동으로 UPDATE
    // return productRepository.save(product);    // update
    return old;
  }

  @Override
  @Transactional 
  public void delete(String id) {
    productRepository.deleteById(id);
  }

  @Override
  public Product select(Long productNo) {
    return productRepository.findById(productNo).orElse(null);
  }

  
  
}
