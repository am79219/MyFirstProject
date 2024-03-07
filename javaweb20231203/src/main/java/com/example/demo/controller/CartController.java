package com.example.demo.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/cart")
public class CartController {
	
	@GetMapping("/loginCart") 
	public ModelAndView loginCart() {
		return new ModelAndView("/cart/cartList");
	}
	

}
