package com.example.demo.model.vo;

import lombok.Data;

/*
 * 	2. 產品(Product)
		(1) productId (4001,4002,...)
		(2) productName
		(3) price
		(4) isLaunch: 是否上架(1上架/0未上架)
		(5) category: 1(蔬菜),2(水果),3(其他)
		(6) stock: 庫存數量
		(7) picName: 圖片檔名
 */

@Data
public class Product {
	private Integer productId;
	private String productName;
	private Integer price;
	private Integer isLaunch;
	private Integer category;
	private Integer stock;
	private String picName;
	
	public Product(String productName, Integer price, Integer isLaunch, Integer category, Integer stock) {
		super();
		this.productName = productName;
		this.price = price;
		this.isLaunch = isLaunch;
		this.category = category;
		this.stock = stock;
		
		//productId由資料庫產生
		//picName於上傳圖片時產生
	}
	
	public Product() {
	}
}
