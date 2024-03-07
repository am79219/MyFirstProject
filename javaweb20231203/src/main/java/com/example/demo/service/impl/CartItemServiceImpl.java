package com.example.demo.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.CartItemMapper;
import com.example.demo.mapper.CartMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.dto.CartItemDto;
import com.example.demo.model.dto.CartDto;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.vo.Cart;
import com.example.demo.model.vo.CartItem;
import com.example.demo.service.CartItemService;
import com.example.demo.service.CartService;

import jakarta.servlet.http.HttpSession;

@Service
public class CartItemServiceImpl implements CartItemService {	
	@Autowired
	private CartMapper cartMapper;
	
	@Autowired
	private CartItemMapper cartItemMapper;
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private HttpSession session;
	
	@Override
	public void addAnCartItemDto(CartItemDto newCartItemDto) {
		//檢查session，做為判斷的依據(以下3個session，都是在登入時建立)
		UserDto userDto =(UserDto)session.getAttribute("userDto");
		CartDto uncheckedCartDto =(CartDto)session.getAttribute("uncheckedCartDto");
		Map<Integer,CartItemDto> cartItemDtoMap =(Map<Integer,CartItemDto>)session.getAttribute("cartItemDtoMap");
		
		//1. (會員及非會員)
		// 若cartItemDtos為null，產生新的物件
		if(cartItemDtoMap==null) {
			cartItemDtoMap=new HashMap<>();
		}
		// 判斷新加入購物車的這項產品，是否和原有的productId重複
		// 若重複則修改數量
		for(Map.Entry<Integer,CartItemDto> entry : cartItemDtoMap.entrySet()){
			Integer productId = entry.getKey();
			CartItemDto cartItemDto = entry.getValue();
			if(newCartItemDto.getProductId().equals(productId)) {
				// 將修改後的 cartItemDto 放回原始映射中
				cartItemDto.setQuantity(cartItemDto.getQuantity()+newCartItemDto.getQuantity());
				break;
			}
		}
		
		// 若未重複則put新的一筆
		updateProductInfoForCartItemDto(newCartItemDto);
		cartItemDtoMap.putIfAbsent(newCartItemDto.getProductId(), newCartItemDto);
		
		// 更新Session-cartItemDtoMap
		session.setAttribute("cartItemDtoMap", cartItemDtoMap);
		
		//2. (會員身分)
		if(userDto != null) {
			System.out.println("會員購買一件商品!");
			// 若沒有購物車，建立新的購物車，並更新Session-uncheckedCartDto
			if(uncheckedCartDto == null) {
				cartService.createNewCart(userDto.getUserId());
				uncheckedCartDto = cartService.getNotcheckedCartDtoByUserId(userDto.getUserId());
				session.setAttribute("uncheckedCartDto", uncheckedCartDto);
			}
			
			// 將購物明細加入資料庫 (單筆)
			updateCartItemOrCreateNewToDB(newCartItemDto, uncheckedCartDto.getCartId());
		}
	}
	
	@Override
	//適用情況: 使用者於登入前購物
	public void saveCartInfoToDB(Map<Integer,CartItemDto> cartItemDtoMap, Integer userId) {
		System.out.println("該使用者於登入前有購物行為");
		// 查詢DB是否有未結帳購物車
		Optional<Cart> cartOpt= cartMapper.getNotcheckedCartByUserId(userId);
		// 若DB無未結帳購物車
		Cart cart;
		if(cartOpt.isEmpty()) {
			//在資料庫建立新的購物車
			cartService.createNewCart(userId);
			cartOpt= cartMapper.getNotcheckedCartByUserId(userId);
			cart = cartOpt.get();
		}else {
			cart = cartOpt.get();
		}
		
		// 將購物明細加入資料庫 (多筆/Map)
		updateCartItemOrCreateNewToDB(cartItemDtoMap,cart.getCartId());
	}

	@Override
	public Map<Integer,CartItemDto> findCartItemDtosByCartId(Integer cartId){
		List<CartItem> cartItems = cartItemMapper.findCartItemsByCartId(cartId);
		Map<Integer,CartItemDto> cartItemDtoMap = new HashMap<>();
		for(CartItem cartItem : cartItems) {
			cartItemDtoMap.put(cartItem.getProductId(),
					modelMapper.map(cartItem,CartItemDto.class));
		}
		return cartItemDtoMap;
	}
	
	@Override
	public void updateProductInfoForCartItemDto(CartItemDto cartItemDto) {
		cartItemDto.setProduct(productMapper.getProductById(cartItemDto.getProductId()).get());
	}
	
	@Override
	public Boolean deleteCartItemById(Integer cartItemId) {
		// TODO Auto-generated method stub
		return false;
	}

	//以下為private方法
	//將購物車資料新增至DB(多筆)
	private void updateCartItemOrCreateNewToDB(Map<Integer,CartItemDto> cartItemDtoMap, Integer cartId) {
		for(CartItemDto cartItemDto: cartItemDtoMap.values()) {
			// 判斷資料庫內同一台購物車內，是否有相同的商品
			// 有的話，update-->資料庫
			Optional<CartItem> SameProductOpt = cartItemMapper.ifSameProductInCart(cartItemDto.getProductId(),cartId);
			if(SameProductOpt.isPresent()) {
				CartItem SameProduct = SameProductOpt.get();
				cartItemMapper.updateCartItemQuantity(SameProduct.getCartItemId(), SameProduct.getQuantity()+cartItemDto.getQuantity());
			// 沒有的話，add-->資料庫
			}else {
				cartItemDto.setCartId(cartId);
				CartItem cartItem = modelMapper.map(cartItemDto, CartItem.class);
				cartItemMapper.addCartItemForUser(cartItem);
			}	
		}
	}
	
	//將購物車資料新增至DB(單筆)
	private void updateCartItemOrCreateNewToDB(CartItemDto cartItemDto, Integer cartId) {
		// 判斷資料庫內同一台購物車內，是否有相同的商品
		// 有的話，update-->資料庫
		Optional<CartItem> SameProductOpt = cartItemMapper.ifSameProductInCart(cartItemDto.getProductId(),cartId);
		if(SameProductOpt.isPresent()) {
			CartItem SameProduct = SameProductOpt.get();
			cartItemMapper.updateCartItemQuantity(SameProduct.getCartItemId(), SameProduct.getQuantity()+cartItemDto.getQuantity());
		// 沒有的話，add-->資料庫
		}else {
			cartItemDto.setCartId(cartId);
			CartItem cartItem = modelMapper.map(cartItemDto, CartItem.class);
			cartItemMapper.addCartItemForUser(cartItem);
		}
	}
}
