package com.example.demo.model.dto;

import java.sql.Date;
import java.util.List;

import com.example.demo.model.vo.CartItem;
import com.example.demo.model.vo.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
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
}
