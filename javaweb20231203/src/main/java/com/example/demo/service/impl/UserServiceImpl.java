package com.example.demo.service.impl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.vo.User;
import com.example.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Boolean addUser(UserDto userDto) {
		Optional<User> userOpt = userMapper.getUserByUsername(userDto.getUsername());
		if(userOpt.isEmpty()) {
			User user = modelMapper.map(userDto, User.class);
			userMapper.addUser(user);
			return true;
		}
		return false;
	}
	
	@Override
	public UserDto loginConfirm(String username, String password) {
		Optional<User> userOpt = userMapper.getUserByUsername(username);
		if(userOpt.isPresent()) {
			User user = userOpt.get();
			if(user.getPassword().equals(password)) {
				return modelMapper.map(user,UserDto.class);
			}			
		}
		return null;
	}

	@Override
	public UserDto loginConfirmForManage(String username, String password) {
		Optional<User> userOpt = userMapper.getUserByUsernameForManage(username);
		if(userOpt.isPresent()) {
			User user = userOpt.get();
			if(user.getPassword().equals(password)) {
				return modelMapper.map(user,UserDto.class);
			}	
		}
		return null;
	}
	
	@Override
	public Boolean isUsernameDuplicate(String username) {
		Optional<User> userOpt = userMapper.getUserByUsername(username);
		if(userOpt.isPresent()) {
			return true;
		}
		return false;
	}


}
