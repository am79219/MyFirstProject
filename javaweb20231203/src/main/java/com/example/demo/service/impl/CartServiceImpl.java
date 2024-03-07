package com.example.demo.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.CartItemMapper;
import com.example.demo.mapper.CartMapper;
import com.example.demo.model.dto.CartDto;
import com.example.demo.model.vo.Cart;
import com.example.demo.model.vo.CartItem;
import com.example.demo.service.CartItemService;
import com.example.demo.service.CartService;

import jakarta.servlet.http.HttpSession;

@Service

public class CartServiceImpl implements CartService {
	@Autowired
	private CartMapper cartMapper;
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public void createNewCart(Integer userId) {
		//1. 建立購物車物件-填入userId，設定isCheckout為false
		Cart cart = new Cart(userId);
		//2. 將此購物車物件新增至資料庫，此時資料庫自動產生cartId(Auto Increment)
		cartMapper.addCart(cart);
		//3. 從資料庫回查cartId
		int cartId=cartMapper.getNotcheckedCartByUserId(userId).get().getCartId();
		//4. 取得cartNo (命名規則:當天日期_userId_cartId)
		String cartNo=setCartNo(cartId, userId);
		//5. 將cartNo更新至資料庫
		cartMapper.updateCartNoByCartId(cartNo, cartId);
	}
	
	@Override
	public CartDto getNotcheckedCartDtoByUserId(Integer userId) {
		Optional<Cart> cartOpt = cartMapper.getNotcheckedCartByUserId(userId);
		if(cartOpt.isPresent()) {
			return modelMapper.map(cartOpt.get(),CartDto.class);
		}
		return null;
	}
	
	//以下為private方法
	private String setCartNo(Integer cartId, Integer userId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String today = sdf.format(new java.util.Date());
		
		return today+"_"+userId+"_"+String.valueOf(cartId);
	}
}
