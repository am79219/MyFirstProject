package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.dto.UserDto;
import com.example.demo.model.dto.CartDto;
import com.example.demo.model.dto.CartItemDto;
import com.example.demo.service.UserService;
import com.example.demo.service.CartService;
import com.example.demo.service.CartItemService;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")

public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private HttpSession session;
	
	@PostMapping("/login")
	public ModelAndView login(String username,String password) {
		ModelAndView mav = null;
		UserDto userDto = userService.loginConfirm(username, password);
		//1. 帳密正確，登入成功
		if(userDto!=null) {
			//A. 建立session-userDto
			session.setAttribute("userDto", userDto);
			
			//B. 判斷會員在登入前是否有購物行為
			Map<Integer,CartItemDto> cartItemDtoMap = (Map<Integer,CartItemDto>) session.getAttribute("cartItemDtoMap");
			if(cartItemDtoMap!=null) {
				// 有的話，將登入前的購物行為，儲存至資料庫
				cartItemService.saveCartInfoToDB(cartItemDtoMap,userDto.getUserId());
			}
			
			//C. 資料庫是否有未結帳購物車
			CartDto uncheckedCartDto = cartService.getNotcheckedCartDtoByUserId(userDto.getUserId());
			if(uncheckedCartDto!=null) {
				//有的話
				//a. 建立session-UncheckedCartDto
				session.setAttribute("uncheckedCartDto", uncheckedCartDto);
				
				//b. 判斷該購物車內是否有商品
				cartItemDtoMap= cartItemService.findCartItemDtosByCartId(uncheckedCartDto.getCartId());
				
				if(cartItemDtoMap!=null) {
					// 有的話
					// 更新每筆購物明細的產品資料(為了給前端渲染)
					for( Map.Entry<Integer,CartItemDto> entry : cartItemDtoMap.entrySet()) {
						CartItemDto cartItemDto = entry.getValue();
						cartItemService.updateProductInfoForCartItemDto(cartItemDto);
					}
					
					
					// 建立session-CartItemDtos
					session.setAttribute("cartItemDtoMap", cartItemDtoMap);
					System.out.println(cartItemDtoMap.size());
				}	
			}
			System.out.println(userDto);
			System.out.println(cartItemDtoMap);
			System.out.println(uncheckedCartDto);
			
			mav = new ModelAndView("/user/loginSuccess");
		}else {
		//2. 登入失敗
			mav = new ModelAndView("/user/loginError");
		}
		return mav;
	}
	
	@PostMapping("/addUser")
	public ModelAndView addUser(UserDto userDto) {
		ModelAndView mav = null;
		if(userService.isUsernameDuplicate(userDto.getUsername())) {
			mav = new ModelAndView("/user/addUserError");
		}else {
			userService.addUser(userDto);
			mav = new ModelAndView("/user/addUserSuccess");
		}
		return mav;
	}
	
	@GetMapping("/toIndex")
	public ModelAndView toIndex() {
		return new ModelAndView("/index");
	}
	
	@GetMapping("/logout")
	public ModelAndView logout() {
		session.removeAttribute("userDto");
		session.removeAttribute("uncheckedCartDto");
		session.removeAttribute("cartItemDtoMap");
		return new ModelAndView("/user/logout");
	}
}
