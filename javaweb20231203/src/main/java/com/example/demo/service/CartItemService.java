package com.example.demo.service;


public interface CartItemService {
	void AddAnCartItem(Integer productId, Integer quantity);
	boolean deleteCartItemById(Integer cartItemId);
}
