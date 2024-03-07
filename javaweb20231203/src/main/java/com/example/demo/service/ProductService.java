package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.ProductDto;

public interface ProductService {
	//create
	void addAnProductDto(ProductDto productDto);
	
	//read
	List<ProductDto> findAllProductDtos();
	List<ProductDto> findAvailableProductDtosByCategory(Integer category);
	ProductDto getProductDtoById(Integer productId);
	
	//update
	void updateProductById(ProductDto productDto, Integer productId);
	void updateProductPicNameById(String picName, Integer productId);
	
	//delete
}
