package com.example.demo.service;

import com.example.demo.model.dto.CartDto;
import com.example.demo.model.vo.Cart;

public interface CartService {
	//create
	void createNewCart(Integer userId);
	
	//read
	CartDto getNotcheckedCartDtoByUserId(Integer userId);
	
	//update
	
	//delete

}
