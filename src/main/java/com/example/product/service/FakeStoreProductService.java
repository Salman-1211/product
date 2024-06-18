package com.example.product.service;

import com.example.product.dtos.ProductRequestDto;
import com.example.product.dtos.ProductResponseDto;
import com.example.product.models.Category;
import com.example.product.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service

public class FakeStoreProductService implements IProductService{

    @Autowired
    RestTemplate restTemplate;

    public Product getProdcutFromResponseDto(ProductResponseDto responseDto){
        Product product = new Product();
        product.setId(responseDto.getId());
        product.setName(responseDto.getTitle());
        product.setPrice(responseDto.getPrice());
        product.setDescription(responseDto.getDescription());
        product.setImage(responseDto.getImage());
        product.setCategory(new Category());

       /* product.getCategory().setName(responseDto.getCategory());*/
       // Alternate method to  write
        Category category = new Category();
        category.setName(responseDto.getCategory());

        product.setCategory(category);
        return product;
    }
    @Override
    public Product getSingleProduct(Long id) throws InvalidProductIdException {
        // I should pass this 'id' to fakestore & get the details of this product
        // 'https://fakestoreapi.com/products/1'
        if(id>20){
            throw new InvalidProductIdException();
        }

        ProductResponseDto response =
                restTemplate.getForObject("https://fakestoreapi.com/products/" + id,
                ProductResponseDto.class);

        return getProdcutFromResponseDto(response);
    }

    @Override
    public List<Product> getAllProducts() {
       // return null;
       ProductResponseDto[] responseDtoList= restTemplate.getForObject("https://fakestoreapi.com/products/",
             ProductResponseDto[].class);

       List<Product> output = new ArrayList<>();
       for(ProductResponseDto productResponseDto: responseDtoList){
           output.add(getProdcutFromResponseDto(productResponseDto));
       }
       return output;
    }

    @Override
    public Product updateProduct(Long id, ProductRequestDto productRequestDto) {

        /*
          Method to call -> PUT, it'll lead to two network calls.
         */

        RequestCallback requestCallback = restTemplate.httpEntityCallback(productRequestDto, ProductResponseDto.class);
        HttpMessageConverterExtractor<ProductResponseDto> responseExtractor =
                new HttpMessageConverterExtractor<>(ProductResponseDto.class,
                        restTemplate.getMessageConverters());
        ProductResponseDto responseDto = restTemplate.execute("https://fakestoreapi.com/products/" + id,
                HttpMethod.PUT, requestCallback, responseExtractor);

        return getProdcutFromResponseDto(responseDto);


        //This is not returning the object that it has modified.
     //   restTemplate.put("https://fakestoreapi.com/products/" +id, productRequestDto);

        // But, I want to get the updated object.
       // getSingleProduct(id);
    }
}
