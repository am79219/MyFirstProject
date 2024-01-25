package com.example.demo.mapper;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.vo.Product;

@Mapper

public interface ProductMapper {
	//Create
	@Insert("insert into product(productName,price,isLaunch,category,stock,picName)"
			+ " values(#{productName},#{price},#{isLaunch},#{category},#{stock},#{picName})")
	void add(String productName, Integer price, Integer isLaunch, Integer category, Integer stock, String picName);
	
	//Read
	@Select("select productId, productName, price, isLaunch, category, stock, picName from product")
	List<Product> findAll();
	
	@Select("select productId, productName, price, isLaunch, category, stock, picName from product where productId = #{productId}")
	Product getProductById(Integer productId);
	
	@Select("select productId, productName, price, isLaunch, category, stock, picName from product where category = #{category} and isLaunch = true order by productId")
	List<Product> findIsLaunchProductsByCategory(Integer category);
	
	//Update
	@Update("update product set productName=#{productName}, price=#{price}, isLaunch=#{isLaunch}, category=#{category}, stock=#{stock} where productId=#{productId}")
	void updateProductById(String productName, Integer price, Integer isLaunch, Integer category, Integer stock, Integer productId);
	
	@Update("update product set picName=#{picName} where productId=#{productId}")
	void updatePicNameById(String picName, Integer productId);

}
