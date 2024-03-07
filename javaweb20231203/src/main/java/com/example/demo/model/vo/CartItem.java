package com.example.demo.model.vo;

import lombok.Data;

/*
 * 	4. 購物車明細(CartItem)
		(1) cartItemId (1,2,...)
		(2) cartId          ==>Cart
		(3) productId 		==>Product
		(4) quantity
 */

@Data
public class CartItem {
	private Integer cartItemId;
	private Integer cartId;
	private Integer productId;
	private Integer quantity;
	
	private Product product; //產品物件
	private Cart cart; //購物車物件
	
	public CartItem(Integer cartId, Integer productId, Integer quantity) {
		this.cartId = cartId;
		this.productId = productId;
		this.quantity = quantity;
		
		//根據productId找到Product物件 (未完成)
		//根據cartId找到Cart物件 (未完成)
	}
	
	public CartItem(Integer productId, Integer quantity) {
		this.productId = productId;
		this.quantity = quantity;
		
		//根據productId找到Product物件 (未完成)
		//根據cartId找到Cart物件 (未完成)
	}

	public CartItem() {
	}
}
