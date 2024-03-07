package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.dto.CartItemDto;
import com.example.demo.service.CartItemService;


@RestController
@RequestMapping("/cartitem")

public class CartItemController {
	@Autowired
	private CartItemService cartItemService;
		
	@PostMapping("/addAnCartItem")
	public ModelAndView addAnCartItem(Integer productId,Integer quantity) {		
		CartItemDto cartItemDto = new CartItemDto();
		cartItemDto.setProductId(productId);
		cartItemDto.setQuantity(quantity);
		cartItemService.addAnCartItemDto(cartItemDto);
	    return new ModelAndView("/cart/cartList");
	}
	
	@GetMapping("/deleteAnCartItem")
	public ModelAndView deleteAnCartItem(Integer cartItemId) {
		//cartItemService.deleteAnCartItemDto(cartItemId); //方法尚未寫好
		return new ModelAndView("/cart/cartList");
	}

}
