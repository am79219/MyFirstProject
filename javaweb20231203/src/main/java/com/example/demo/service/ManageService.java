package com.example.demo.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface ManageService {
	void SaveProductPicture(MultipartFile productFile) throws IOException;
}
