package com.example.demo.vo;

import java.sql.Date;
import java.util.List;

import lombok.Data;
import java.text.SimpleDateFormat;

/*
 * 	3. 購物車(Cart)
		(1) cartId (8001,8002,...)
		(2) cartNo: 購物車編號
		            ex.20231203_101_8001(命名規則:日期_userId_cartId)
		(3) userId    		==>User
	   *(4) cartItems       ==>多筆CartItem
		(5) isCheckout: 是否結帳(0/1)
		(6) checkoutTime: 結帳時間
		(7) isShipping: 是否出貨(0/1)
		(8) shippingTime: 出貨時間
		(9) isArrived: 是否到貨(0/1)
		(10) arrivedTime: 到貨時間
 */

@Data

public class Cart {
	private Integer cartId;
	private String cartNo;
	private Integer userId;
	private Boolean isCheckout;
	private Date checkoutTime;
	private Boolean isShipping;
	private Date shippingTime;
	private Boolean isArrived;
	private Date arrivedTime;
	
	private User user; //使用者物件
	private List<CartItem> cartItems; //購物車明細
	
	public Cart(Integer userId) {
		this.userId = userId;
		this.isCheckout = false; //預設結帳狀態為false
		this.isShipping = false; //預設出貨狀態為false
		this.isArrived = false; //預設到貨狀態為false
		
		//根據userId找到User物件 (未完成)
	}
	public Cart() {
	}
	
	public void setCartNo(Integer cartId, Integer userId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String today = sdf.format(new java.util.Date());
		
		this.cartNo= today+"_"+userId+"_"+String.valueOf(cartId);
	}
	
	
}
