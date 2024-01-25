package com.example.demo.service.impl;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.CartItemMapper;
import com.example.demo.mapper.CartMapper;
import com.example.demo.service.CartItemService;
import com.example.demo.vo.CartItem;
import com.example.demo.vo.User;
import com.example.demo.vo.Cart;

import jakarta.servlet.http.HttpSession;

@Service
public class CartItemServiceImpl implements CartItemService {
	@Autowired
	private CartMapper cm;
	
	@Autowired
	private CartItemMapper cim;
	
	@Autowired
	private HttpSession session;
	

	@Override
	public void AddAnCartItem(Integer productId, Integer quantity) {
		//檢查session，做為判斷的依據(以下3個session，都是在登入時建立)
		User user =(User)session.getAttribute("User"); //是否有登入
		Cart cart =(Cart)session.getAttribute("Cart"); //是否有未結帳購物車
		List<CartItem> cartItems =(List<CartItem>)session.getAttribute("CartItems"); //未結帳購物車明細List
		System.out.println(user);
		System.out.println(cart);
		System.out.println(cartItems);
		
		
		//1.使用者未登入
		if(user == null) {
			//A.若購物清單是空的-->建立一個List來收集資料
			if(cartItems==null) {
				cartItems=new ArrayList<>();
			}
			//【Both】List增加一筆明細進去
			cartItems.add(new CartItem(productId,quantity));
			//【Both】更新session
			session.setAttribute("CartItems", cartItems);
		}else {
		//2.使用者已登入
			System.out.println("User已登入");
			Integer cartId;
			//A.若還沒有未結帳購物車，新增一台購物車(系統依規則建立購物車單號)
			if(cart==null) { 
				//建立購物車物件-填入userId，設定isCheckout為false
				cart = new Cart(user.getUserId());
				//將此購物車物件新增至資料庫，此時資料庫自動產生cartId(Auto Increment)
				cm.addCart(cart);
				//從資料庫回查cartId
				cartId=cm.getNotcheckedCartByUserId(user.getUserId()).get().getCartId();
				//更新購物車物件，將cartId填入
				cart.setCartId(cartId);
				//更新購物車物件，將cartId及userId填入，以取得cartNo(命名規則:日期_userId_cartId)
				cart.setCartNo(cartId,user.getUserId());
				//更新資料庫，填入cartId
				cm.updateCartNoByCartId(cart.getCartNo(), cartId);
				//更新session
				session.setAttribute("Cart", cart);
				//New購物車明細(原本是null)
				System.out.println("cartItems:"+cartItems);
				cartItems=new ArrayList<>();
				System.out.println("New ArrayList後，cartItems:"+cartItems);
			
			//B.已有未結帳購物車，則先取得cartId
			}else {
				cartId=cart.getCartId();
			}
			
			//【Both】建立一筆購物車明細物件(建構式包含cartId)
			CartItem cartItem = new CartItem(cartId,productId,quantity);
			//【Both】List增加一筆明細進去
			cartItems.add(cartItem);
			//【Both】更新資料庫 (有登入者才會進資料庫)
			cim.addCartItemForUser(cartItem);
			//【Both】更新session
			session.setAttribute("CartItems", cartItems);
		}
	}

	@Override
	public boolean deleteCartItemById(Integer cartItemId) {
		// TODO Auto-generated method stub
		return false;
	}

}
