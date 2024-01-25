package com.example.demo.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.vo.CartItem;
import com.example.demo.vo.User;

@Mapper

public interface CartItemMapper {
	//Create
	//給登入者使用-->有登入才會保留購物清單(進資料庫)
	@Insert("insert into cartItem(cartId,productId,quantity)"
			+ "values(#{cartId},#{productId},#{quantity})")
	void addCartItemForUser(CartItem cartItem);
		
	//Read
	@Select("select cartItemId,cartId,productId,quantity from cartItem"
			+ "where cartItemId=#{cartItemId}")
	Optional<CartItem> getCartItemById(Integer cartItemId);
	
	@Select("select cartItemId,cartId,productId,quantity from cartItem "
			+ "where cartId=#{cartId} order by cartItemId")
	List<CartItem> findCartItemsByCartId(Integer cartId);
	
	//Delete
	@Delete("delete from cartItem where cartItemId=#{cartItemId}")
	boolean deleteCartItemById(Integer cartItemId);
}
