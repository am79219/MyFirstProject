package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.dto.ProductDto;
import com.example.demo.model.vo.Product;
import com.example.demo.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public void addAnProductDto(ProductDto productDto) {
		Product product = modelMapper.map(productDto,Product.class);
		productMapper.add(product);
	}

	
	@Override
	public List<ProductDto> findAllProductDtos() {
		return productMapper.findAll()
				.stream()
				.map(product->modelMapper.map(product,ProductDto.class))
				.toList();
	}
	
	@Override
	public List<ProductDto> findAvailableProductDtosByCategory(Integer category) {
		return productMapper.findAvailableProductsByCategory(category)
				.stream()
				.map(product->modelMapper.map(product,ProductDto.class))
				.toList();
	}

	@Override
	public ProductDto getProductDtoById(Integer productId) {
		Optional<Product> productOpt = productMapper.getProductById(productId);
		if(productOpt.isPresent()) {
			Product product = productOpt.get();
			ProductDto productDto = modelMapper.map(product, ProductDto.class);
			return productDto;
		}
		return null;
	}


	@Override
	public void updateProductById(ProductDto productDto, Integer productId) {
		Optional<Product> productOpt = productMapper.getProductById(productId);
		System.out.println("productOpt:"+productOpt);
		if(productOpt.isPresent()) {
			Product product = productOpt.get();
			System.out.println(product);
			product.setProductName(productDto.getProductName());
			product.setPrice(productDto.getPrice());
			product.setIsLaunch(productDto.getIsLaunch());
			product.setCategory(productDto.getCategory());
			product.setStock(productDto.getStock());
			System.out.println(product);
			productMapper.updateProductById(product, productId);
			return;
		}
		throw new RuntimeException("產品更新失敗");
	}


	@Override
	public void updateProductPicNameById(String picName, Integer productId) {
		Optional<Product> productOpt = productMapper.getProductById(productId);
		if(productOpt.isPresent()) {
			Product product = productOpt.get();
			product.setPicName(picName);
			productMapper.updatePicNameById(picName, productId);
			return;
		}
		throw new RuntimeException("產品圖片更新失敗");
	}
}
