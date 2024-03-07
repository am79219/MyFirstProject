package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.dto.ProductDto;
import com.example.demo.service.ProductService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/product")

public class ProductController {
	@Autowired
	private ProductService productService;
	
	@Autowired
	private HttpSession session;
	
	//從首頁進入產品頁面(vegetables/fruits/others)
	@GetMapping("/{category}")
	public ModelAndView toProductPage(@PathVariable("category") String category, Model model) {
		switch(category) {
			case "vegetables":
			case "fruits":
			case "others":
				int type = 0;
				if(category.equals("vegetables")) type=1;
				else if(category.equals("fruits")) type=2;
				else if(category.equals("others")) type=3;
				
				//依照類別查詢已上架的產品資料，渲染給前端
				List<ProductDto> productDtos = productService.findAvailableProductDtosByCategory(type);
				model.addAttribute("productDtos", productDtos);
				
				//將商品類別改成第一個字為大寫，渲染給前端
				category = Character.toUpperCase(category.charAt(0)) + category.substring(1);
				model.addAttribute("category",category);
				return new ModelAndView("/product/productList");
			default:
				String errorMessage = "產品路徑錯誤";
				model.addAttribute("errorMessage",errorMessage);
				return new ModelAndView("/error");
		}
	}
	
	@GetMapping("/info/{productId}")
	public ModelAndView toProductInfo(@PathVariable("productId") Integer productId, Model model) {
		ProductDto productDto = productService.getProductDtoById(productId);
		if(productDto==null) {
			String errorMessage = "產品路徑錯誤";
			model.addAttribute("errorMessage",errorMessage);
			return new ModelAndView("/error");
		}
		model.addAttribute("productDto", productDto);
		return new ModelAndView("/product/productInfo");
	}
}
