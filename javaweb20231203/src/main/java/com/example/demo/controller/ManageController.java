package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.ManageService;
import com.example.demo.service.UserService;
import com.example.demo.vo.Product;
import com.example.demo.vo.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage")
public class ManageController {
	@Autowired
	private ProductMapper pm;
	@Autowired
	private UserMapper um;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private HttpSession session;
	@Autowired
	private UserService us;
	@Autowired
	private ManageService ms;
	
	//0. 管理者登入-驗證帳號密碼-切換到menu
	@PostMapping("/menu")
	public ModelAndView toManageMenu(String username,String password) {
		if(us.loginInfoConfirmForManage(username, password)) {
			User manage = um.getUserByUsernameForManage(username);
			session.setAttribute("Manage",manage);
			return new ModelAndView("/manage/menu");
		}
		return new ModelAndView("/manage/loginError");
	}
	
	//0. 切換到menu(已登入狀態)
	@GetMapping("/toMenu")
	public ModelAndView toMenu() {
		if(session.getAttribute("Manage")!=null) {
			return new ModelAndView("/manage/menu");
		}
		return new ModelAndView("/manage/error");
	}
	
	//0. 登出
	@GetMapping("/logout")
	public void logout() throws IOException {
		session.removeAttribute("Manage");
		response.sendRedirect("/manage.html");
	}
	
	//1. 單純切換頁面-產品上傳頁
	@GetMapping("/product/uploadProduct")
	public ModelAndView toUploadProduct() {
		return new ModelAndView("/manage/product/uploadProduct");
	}
	
	//2. 上傳產品資料(包含圖片)
	@PostMapping(value = "/product/upload", 
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ModelAndView uploadProduct(
			@RequestPart(value = "productFile") MultipartFile productFile, 
			String productName, int price, int isLaunch, int category, int stock) 
					throws IOException, ServletException {
		//將圖片存到指定位置(專案或本地端)
		ms.SaveProductPicture(productFile);
				
		//新增該筆資料到資料庫
		String picName=productFile.getOriginalFilename();
		pm.add(productName, price, isLaunch, category, stock ,picName);
		
		//切換到所有上傳成功頁面
		return new ModelAndView("/manage/product/uploadSuccess");
	}
	
	//3. 產品總覽
	@GetMapping("/product/list")
	public ModelAndView productList() {
		List<Product> products = pm.findAll();
		session.setAttribute("Products", products);
		
		//切換到所有產品清單頁面
		return new ModelAndView("/manage/product/list");
	}
	
	//4. 切換到產品更改頁面
	@GetMapping("/product/update/{productId}")
	public ModelAndView toUpdate(@PathVariable("productId") Integer productId) {
		Product product = pm.getProductById(productId);
		session.setAttribute("p",product);
		return new ModelAndView("/manage/product/update");
	}
	
	//5. 更新單筆資料，回產品總覽頁
	@PostMapping("/product/updateConfirm")
	public ModelAndView updateConfirm(Product p) {
		//更新單筆資料
		pm.updateProductById(p.getProductName(), p.getPrice(), p.getIsLaunch(), p.getCategory(), p.getStock(), p.getProductId());
		
		//重新查詢所有產品清單
		List<Product> products = pm.findAll();
		session.setAttribute("Products", products);
		
		//切換到產品總覽頁
		return new ModelAndView("/manage/product/list");
	}
	
	//6. 切換到更改產品圖片頁面
	@GetMapping("/product/picUpdate/{productId}")
	public ModelAndView topicUpdate(@PathVariable("productId") Integer productId) {
		Product product = pm.getProductById(productId);
		session.setAttribute("p",product);
		return new ModelAndView("/manage/product/picUpdate");
	}
	
	//7. 上傳並更新產品圖片，回產品總覽頁
	@PostMapping(value = "/product/picUpdateConfirm",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ModelAndView picUpdateConfirm(
			@RequestPart(value = "productFile") MultipartFile productFile, 
			Integer productId) throws IOException {
		
		//將圖片存到指定位置(專案或本地端)
		ms.SaveProductPicture(productFile);
		
		//修改資料庫的picName
		String picName=productFile.getOriginalFilename();
		pm.updatePicNameById(picName,productId);
		
		//重新查詢所有產品清單
		List<Product> products = pm.findAll();
		session.setAttribute("Products", products);
		
		//切換到產品總覽頁
		return new ModelAndView("/manage/product/list");
	}

}
