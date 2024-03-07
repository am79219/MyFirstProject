package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.dto.ProductDto;
import com.example.demo.model.dto.UserDto;
import com.example.demo.service.ManageService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage")
public class ManageController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ManageService manageService;
	
	@Autowired
	private HttpServletResponse response;
	
	@Autowired
	private HttpSession session;

	//1. 管理者登入-驗證帳號密碼-切換到menu
	@PostMapping("/menu")
	public ModelAndView toManageMenu(String username, String password, Model model) {
		UserDto manageDto = userService.loginConfirmForManage(username, password);
		if(manageDto!=null) {
			session.setAttribute("manageDto",manageDto);
			return new ModelAndView("/manage/menu");
		}
		String manageError = "管理者登入帳密錯誤";
		model.addAttribute("manageError",manageError);
		return new ModelAndView("/manage/error");
	}
	
	//2. 切換到menu(已登入狀態)
	@GetMapping("/toMenu")
	public ModelAndView toMenu(Model model) {
		if(session.getAttribute("manageDto")!=null) {
			return new ModelAndView("/manage/menu");
		}
		String manageError = "您尚未登入，無法切換至管理者menu";
		model.addAttribute("manageError",manageError);
		return new ModelAndView("/manage/error");
	}
	
	//3. 登出
	@GetMapping("/logout")
	public void logout() throws IOException {
		session.removeAttribute("manageDto");
		response.sendRedirect("/manage.html");
	}
	
	//4. 切換到產品上傳頁
	@GetMapping("/product/uploadProduct")
	public ModelAndView toUploadProduct(Model model) {
		if(session.getAttribute("manageDto")!=null) {
			return new ModelAndView("/manage/product/uploadProduct");
		}
		String manageError = "您尚未登入，無法切換至產品上傳頁";
		model.addAttribute("manageError",manageError);
		return new ModelAndView("/manage/error");
	}
	
	//4.1 上傳產品資料(包含圖片)
	@PostMapping(value = "/product/upload", 
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ModelAndView uploadProduct(
			@RequestPart(value = "productFile") MultipartFile productFile, 
			ProductDto productDto, Model model) 
					throws IOException, ServletException {
		//將圖片存到指定位置(專案或本地端)
		manageService.SaveProductPicture(productFile);
				
		//新增該筆資料到資料庫
		String picName=productFile.getOriginalFilename();
		productDto.setPicName(picName);
		productService.addAnProductDto(productDto);
		
		//切換到產品上傳成功頁面
		return new ModelAndView("/manage/product/uploadSuccess");
	}
	
	//5. 切換到產品清單頁
	@GetMapping("/product/list")
	public ModelAndView productList(Model model) {
		if(session.getAttribute("manageDto")!=null) {
			List<ProductDto> productDtosForManage = productService.findAllProductDtos();
			model.addAttribute("productDtosForManage", productDtosForManage);
			//切換到所有產品清單頁
			return new ModelAndView("/manage/product/list");
		}
		String manageError = "您尚未登入，無法切換至產品清單頁";
		model.addAttribute("manageError",manageError);
		return new ModelAndView("/manage/error");
	}
	
	//6. 切換到單筆產品修改頁
	@GetMapping("/product/update/{productId}")
	public ModelAndView toUpdate(@PathVariable("productId") Integer productId, Model model) {
		if(session.getAttribute("manageDto")!=null) {
			ProductDto productDtoForModify = productService.getProductDtoById(productId);
			model.addAttribute("productDtoForModify",productDtoForModify);
			//切換到單筆產品修改頁
			return new ModelAndView("/manage/product/update");
		}
		String manageError = "您尚未登入，無法切換至產品修改頁";
		model.addAttribute("manageError",manageError);
		return new ModelAndView("/manage/error");
	}
	
	//6.1 更新單筆資料，回產品清單頁
	@PostMapping("/product/updateConfirm")
	public void updateConfirm(ProductDto productDto, Integer productId) throws IOException {
		System.out.println(productDto.toString());
		
		//更新單筆資料
		productService.updateProductById(productDto, productId);
		
		//重導:切換到產品清單頁
		response.sendRedirect("/manage/product/list");
	}
	
	//7. 切換到單筆產品圖片修改頁
	@GetMapping("/product/picUpdate/{productId}")
	public ModelAndView toPicUpdate(@PathVariable("productId") Integer productId, Model model) {
		if(session.getAttribute("manageDto")!=null) {
			
			ProductDto productDtoForModify = productService.getProductDtoById(productId);
			model.addAttribute("productDtoForModify",productDtoForModify);
			//切換到單筆產品圖片修改頁
			return new ModelAndView("/manage/product/picUpdate");
		}
		String manageError = "您尚未登入，無法切換至產品圖片修改頁";
		model.addAttribute("manageError",manageError);
		return new ModelAndView("/manage/error");		
	}
	
	//7.1 上傳並更新單筆產品圖片，回產品清單頁
	@PostMapping(value = "/product/picUpdateConfirm",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void picUpdateConfirm(
			@RequestPart(value = "productFile") MultipartFile productFile, 
			Integer productId) throws IOException {
		
		//將圖片存到指定位置(專案或本地端)
		manageService.SaveProductPicture(productFile);
		
		//修改資料庫的picName
		String picName=productFile.getOriginalFilename();
		productService.updateProductPicNameById(picName, productId);
		
		//重導:切換到產品清單頁
		response.sendRedirect("/manage/product/list");
	}
}
