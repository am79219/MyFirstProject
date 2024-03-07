package com.example.demo.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.model.vo.Product;

@Mapper

public interface ProductMapper {
	//Create
	@Insert("insert into product(productName,price,isLaunch,category,stock,picName)"
			+ " values(#{productName},#{price},#{isLaunch},#{category},#{stock},#{picName})")
	void add(Product product);
	
	//Read
	@Select("select productId, productName, price, isLaunch, category, stock, picName from product")
	List<Product> findAll();
	
	@Select("select productId, productName, price, isLaunch, category, stock, picName from product where category = #{category} and isLaunch = true order by productId")
	List<Product> findAvailableProductsByCategory(Integer category);
	
	@Select("select productId, productName, price, isLaunch, category, stock, picName from product where productId = #{productId}")
	Optional<Product> getProductById(Integer productId);

	//Update
	@Update("update product set productName=#{product.productName}, price=#{product.price}, isLaunch=#{product.isLaunch}, category=#{product.category}, stock=#{product.stock} where productId=#{productId}")
	void updateProductById(@Param("product")Product product, @Param("productId")Integer productId);
	
	@Update("update product set picName=#{picName} where productId=#{productId}")
	void updatePicNameById(String picName, Integer productId);
	
	//Delete
	@Delete("delete from product where productId=#{productId}")
	void deleteProductById(Integer productId);
}
