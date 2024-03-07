package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
	private Integer productId;
	private String productName;
	private Integer price;
	private Integer isLaunch;
	private Integer category;
	private Integer stock;
	private String picName;
	
}
