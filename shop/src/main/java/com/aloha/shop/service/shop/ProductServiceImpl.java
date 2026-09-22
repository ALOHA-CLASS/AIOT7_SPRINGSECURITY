package com.aloha.shop.service.shop;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aloha.shop.domain.shop.Product;
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

  
  
}
