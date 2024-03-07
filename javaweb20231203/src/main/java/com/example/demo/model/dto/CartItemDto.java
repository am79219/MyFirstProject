package com.example.demo.model.dto;

import com.example.demo.model.vo.Cart;
import com.example.demo.model.vo.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
	private Integer cartItemId;
	private Integer cartId;
	private Integer productId;
	private Integer quantity;
	
	private Product product; //產品物件
	private Cart cart; //購物車物件

}
