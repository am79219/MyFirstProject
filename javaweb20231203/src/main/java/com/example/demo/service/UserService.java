package com.example.demo.service;

import com.example.demo.model.dto.UserDto;
import com.example.demo.model.vo.User;

public interface UserService {
	
	//create
	Boolean addUser(UserDto userDto);
	
	//read
	UserDto loginConfirm(String username,String password);
	UserDto loginConfirmForManage(String username,String password);
	Boolean isUsernameDuplicate(String username);
	
	//update
	
	//delete
}
