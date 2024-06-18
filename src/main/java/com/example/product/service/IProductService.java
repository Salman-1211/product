package com.example.product.service;

import com.example.product.dtos.ProductRequestDto;
import com.example.product.models.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProductService {
    public Product getSingleProduct(Long id) throws InvalidProductIdException;

    List<Product> getAllProducts();

    Product updateProduct(Long id, ProductRequestDto productRequestDto);
}
