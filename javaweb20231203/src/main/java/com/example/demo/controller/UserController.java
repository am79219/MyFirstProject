package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.impl.CartServiceImpl;
import com.example.demo.service.impl.UserServiceImpl;
import com.example.demo.vo.User;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import com.example.demo.vo.CartItem;

@RestController
@RequestMapping("/user")

public class UserController {
	@Autowired
	private UserServiceImpl usi;
	
	@Autowired
	private UserMapper um;
	
	@Autowired
	private CartServiceImpl csi;
	
	@Autowired
	private HttpSession session;
	
	@PostMapping("/login")
	public ModelAndView login(String username,String password) {
		ModelAndView mav = null;
		//1. 帳密正確，登入成功
		if(usi.loginInfoConfirm(username, password)) {
			//建立session-User
			User user = um.getUserByUsername(username);
			session.setAttribute("User", user);
			
			//判斷是否在登入前是否有購物行為
			List<CartItem> cartItems = (List<CartItem>) session.getAttribute("CartItems");
			//A. 沒有的話
			if(cartItems==null) {
				//從資料庫查詢該會員是否有購物車及購物車明細資料
				//有的話從資料庫抓出，建立Session_Cart & CartItems
				csi.getNotcheckedCartInfoFromDB(user.getUserId());
			}else {
			//B. 有的話
				//將登入前的購物行為，儲存至資料庫(確保Session_Cart & CartItems是最新資料)
				csi.saveCartInfoToDB(cartItems,user.getUserId());
				
			}
						
			mav = new ModelAndView("/user/loginSuccess");
		}else {
		//2. 登入失敗
			mav = new ModelAndView("/user/loginError");
		}
		return mav;
	}
	
	@PostMapping("/addUser")
	public ModelAndView addUser(String name,String username,String password,String email) {
		ModelAndView mav = null;
		if(um.getUserByUsername(username) != null) {
			mav = new ModelAndView("/user/addUserError");
		}else {
			User user= new User(name,username,password,email,1);
			um.addUser(user);
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
		session.removeAttribute("User");
		session.removeAttribute("Cart");
		session.removeAttribute("CartItems");
		return new ModelAndView("/user/logout");
	}
	
	
}
