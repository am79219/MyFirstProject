package com.example.demo.service;

import java.util.Map;

import com.example.demo.model.dto.CartItemDto;

public interface CartItemService {
	//create
	void addAnCartItemDto(CartItemDto cartItemDto);
	void saveCartInfoToDB(Map<Integer,CartItemDto> cartItemDtoMap, Integer userId);
	
	//read
	Map<Integer,CartItemDto> findCartItemDtosByCartId(Integer cartId);
	
	//update
	void updateProductInfoForCartItemDto(CartItemDto cartItemDto);

	//delete
	Boolean deleteCartItemById(Integer cartItemId);

}
