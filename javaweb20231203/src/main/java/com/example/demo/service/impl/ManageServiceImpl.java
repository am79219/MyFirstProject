package com.example.demo.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.ManageService;

@Service
public class ManageServiceImpl implements ManageService {

	@Override
	public void SaveProductPicture(MultipartFile productFile) throws IOException {
		//觀察MultipartFile提供的方法
		System.out.println(productFile.getContentType());
		System.out.println(productFile.getOriginalFilename());
		
		//決定產品的儲存位置
		//以下是存到專案裡面的資料夾(相對位置)
		String dest="src/main/resources/static/pic/"+productFile.getOriginalFilename();
		System.out.println(dest);
				
		//將圖片存到指定位置(專案或本地端)
		Path path = Paths.get(dest);
		byte[] bytes = productFile.getBytes();
		Files.write(path, bytes);
		System.out.println("存檔成功");
	}

}
