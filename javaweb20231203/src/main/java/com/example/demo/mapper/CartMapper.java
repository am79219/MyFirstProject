package com.example.demo.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.vo.Cart;

@Mapper

public interface CartMapper {
	//Create
	@Insert("insert into cart(cartId, userId, isCheckout, checkoutTime, isShipping, shippingTime, isArrived, arrivedTime)"
			+ "values(#{cartId},#{userId},#{isCheckout},#{checkoutTime},#{isShipping},#{shippingTime},#{isArrived},#{arrivedTime})")
	void addCart(Cart cart);
	
	//Read
	@Select("select cartId, cartNo, userId, isCheckout, checkoutTime, isShipping, shippingTime, isArrived, arrivedTime from cart where userId=#{userId} and isCheckout = false")
	Optional<Cart> getNotcheckedCartByUserId(Integer userId);
	
	//Update
	@Update("update cart set cartNo=#{cartNo} where cartId=#{cartId}")
	Boolean updateCartNoByCartId(String cartNo, Integer cartId);
	
	
	//Delete

}
