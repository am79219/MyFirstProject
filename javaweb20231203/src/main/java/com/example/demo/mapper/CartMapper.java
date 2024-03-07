package com.example.demo.mapper;

import java.sql.Date;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.model.vo.Cart;

@Mapper

public interface CartMapper {
	//Create
	@Insert("insert into cart(cartId, userId, isCheckout, checkoutTime, isShipping, shippingTime, isArrived, arrivedTime)"
			+ "values(#{cartId},#{userId},#{isCheckout},#{checkoutTime},#{isShipping},#{shippingTime},#{isArrived},#{arrivedTime})")
	void addCart(Cart cart);
	
	//Read
	@Select("select cartId, cartNo, userId, isCheckout, checkoutTime, isShipping, shippingTime, isArrived, arrivedTime from cart where userId=#{userId} and isCheckout = false")
	Optional<Cart> getNotcheckedCartByUserId(Integer userId);
	
	@Select("select cartId, cartNo, userId, isCheckout, checkoutTime, isShipping, shippingTime, isArrived, arrivedTime from cart where cartId=#{cartId}")
	Optional<Cart> getCartById(Integer cartId);
	
	//Update
	@Update("update cart set cartNo=#{cartNo} where cartId=#{cartId}")
	void updateCartNoByCartId(String cartNo, Integer cartId);
	
	@Update("update cart set isCheckout=true and checkoutTime=#{checkoutTime} where cartId=#{cartId}")
	void setCheckoutTrueById(Integer id, Date checkoutTime);
	
	@Update("update cart set isShipping=true and shippingTime=#{shippingTime} where cartId=#{cartId}")
	void setIsShippingTrueById(Integer id, Date shippingTime);
	
	@Update("update cart set isArrived=true and arrivedTime=#{arrivedTime} where cartId=#{cartId}")
	void setIsArrivedTrueById(Integer id, Date arrivedTime);
	
	//Delete
	@Delete("delete from cart where cartId=#{cartId}")
	void deleteCartById(Integer id);

}
