package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import com.example.demo.vo.User;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper um;

	@Override
	public boolean loginInfoConfirm(String username, String password) {
		User user = um.getUserByUsername(username);
		if(user != null) {
			return user.getPassword().equals(password);
		}
		return false;
	}

	@Override
	public boolean loginInfoConfirmForManage(String username, String password) {
		User user = um.getUserByUsernameForManage(username);
		if(user != null) {
			return user.getPassword().equals(password);
		}
		return false;
	}
}
