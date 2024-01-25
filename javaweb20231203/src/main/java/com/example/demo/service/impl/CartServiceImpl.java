package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.CartItemMapper;
import com.example.demo.mapper.CartMapper;
import com.example.demo.service.CartService;
import com.example.demo.vo.Cart;
import com.example.demo.vo.CartItem;

import jakarta.servlet.http.HttpSession;

@Service

public class CartServiceImpl implements CartService {
	@Autowired
	private CartMapper cm;
	@Autowired
	private CartItemMapper cim;
	@Autowired
	private HttpSession session;
	
	//會員登入後-->按【購物車】
		//前次有未結帳購物車-->出現購物車明細
		//前次無未結帳購物車-->空的
	
	//適用情況: 使用者登入後再購物
	@Override
	public void getNotcheckedCartInfoFromDB(Integer userId) {
		Optional<Cart> opt= cm.getNotcheckedCartByUserId(userId);
		//1. 如果該會員在資料庫中有未結帳的購物車(會員)
		if(opt.isPresent()) { 
			//取得購物車cartId
			Cart cart = opt.get();
			Integer cartId = cart.getCartId();
			
			//依照cartId取得cartItems (前次登入時保留的購物車明細)
			List<CartItem> cartItems= cim.findCartItemsByCartId(cartId);
			
			//將cart、cartItems的資料以session保存下來
			session.setAttribute("Cart", cart);
			session.setAttribute("CartItems", cartItems);
			System.out.println(cart);
			System.out.println(cartItems);
		}
		//2. 如果在資料庫中沒有未結帳的購物車，不執行任何動作
	}
	
	//適用情況: 使用者先購物再登入(CartItems有資料，但還沒進資料庫)
	//將登入前的購物車明細資料，加進資料庫，並確保目前的Session(Cart，CartItem)是最新的
	public void saveCartInfoToDB(List<CartItem> cartItems, Integer userId) {
		System.out.println("該使用者於登入前有購物行為");
		//查詢DB是否有未結帳購物車
		Optional<Cart> opt= cm.getNotcheckedCartByUserId(userId);
		//1.DB無未結帳購物車
		if(opt.isEmpty()) {
			//【以下和 CartItemServiceImpl 的方法 AddAnCartItem 步驟有重複】
			
			//A. 建立購物車
			//建立購物車物件-填入userId，設定isCheckout為false
			Cart cart = new Cart(userId);
			//將此購物車物件新增至資料庫，此時資料庫自動產生cartId(Auto Increment)
			cm.addCart(cart);
			//從資料庫回查cartId
			int cartId=cm.getNotcheckedCartByUserId(userId).get().getCartId();
			//更新購物車物件，將cartId填入
			cart.setCartId(cartId);
			//更新購物車物件，將cartId及userId填入，以取得cartNo(命名規則:日期_userId_cartId)
			cart.setCartNo(cartId,userId);
			//更新資料庫，填入cartId
			cm.updateCartNoByCartId(cart.getCartNo(), cartId);
			//更新session
			session.setAttribute("Cart", cart);
			System.out.println(cart);
			
			//B. 將登入前的購物行為加入資料庫
			cartItems.stream()
				.forEach(cartItem->{
					System.out.println(cartItem);
					cartItem.setCartId(cartId);
					cim.addCartItemForUser(cartItem);
				});
			
		}
		
		//2.DB有未結帳購物車，先將登入前的購物資料add進資料庫，再撈出全部的購物清單
		else {
			//A. 取得購物車
			Cart cart = opt.get();
			int cartId = cart.getCartId();
			System.out.println(cart);
			session.setAttribute("Cart", cart);
			
			//B. 將登入前的購物行為加入資料庫
			cartItems.stream()
				.forEach(cartItem->{
					System.out.println(cartItem);
					cartItem.setCartId(cartId);
					cim.addCartItemForUser(cartItem);
				});
			//C. 重新從資料庫抓最新的cartItem明細
			cartItems = cim.findCartItemsByCartId(cartId);
			session.setAttribute("CartItems", cartItems);
		}
	}
}
