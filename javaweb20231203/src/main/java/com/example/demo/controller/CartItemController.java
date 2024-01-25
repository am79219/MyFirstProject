package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.example.demo.service.impl.CartItemServiceImpl;


@RestController
@RequestMapping("/cartitem")

public class CartItemController {
	@Autowired
	private CartItemServiceImpl cisi;
	
	
	@PostMapping("/addAnCartItem")
	public ModelAndView addAnCartItem(Integer productId,Integer quantity) {		
		cisi.AddAnCartItem(productId, quantity);
	    return new ModelAndView("/cart/cartList");
	}

}
