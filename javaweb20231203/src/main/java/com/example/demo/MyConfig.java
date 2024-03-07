package com.example.demo;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {
	// 第三方資源
	// 註冊ModelMapper
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
