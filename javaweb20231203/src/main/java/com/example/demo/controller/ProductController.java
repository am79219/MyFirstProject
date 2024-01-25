package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.mapper.ProductMapper;
import com.example.demo.service.impl.ProductServiceImpl;
import com.example.demo.vo.Product;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/product")

public class ProductController {
	@Autowired
	private ProductMapper pm;
	
	@Autowired
	private HttpSession session;
	
	//從首頁進入產品頁面(vegetables/fruits/others)
	@GetMapping("/{category}")
	public ModelAndView toProductPage(@PathVariable("category") String category) {
		switch(category) {
			case "vegetables":
			case "fruits":
			case "others":
				int type = 0;
				if(category.equals("vegetables")) type=1;
				else if(category.equals("fruits")) type=2;
				else if(category.equals("others")) type=3;
				
				//依照類別查詢已上架的產品資料
				List<Product> products = pm.findIsLaunchProductsByCategory(type);
				session.setAttribute("Products", products);
				System.out.println(products);
				return new ModelAndView("/product/"+category);
			default:
				return new ModelAndView("/errorPath");
		}
	}
	
	@GetMapping("/info/{productId}")
	public ModelAndView toProductInfo(@PathVariable("productId") Integer productId) {
		Product p = pm.getProductById(productId);
		if(p==null) {
			return new ModelAndView("/errorPath");
		}
		session.setAttribute("PInfo", p);
		return new ModelAndView("/product/productInfo");
	}
}
