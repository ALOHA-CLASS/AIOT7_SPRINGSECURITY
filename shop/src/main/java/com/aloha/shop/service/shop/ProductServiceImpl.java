package com.aloha.shop.service.shop;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  @Override
  public Page<Product> list(Pageable pageable) {
    return productRepository.page(pageable);
  }

  
  
}
