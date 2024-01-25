package com.example.demo.service;

import java.util.Optional;

import com.example.demo.vo.Cart;

public interface CartService {
	void getNotcheckedCartInfoFromDB(Integer userId);

}
