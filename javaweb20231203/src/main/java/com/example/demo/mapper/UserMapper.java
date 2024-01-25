package com.example.demo.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.demo.vo.User;

@Mapper

public interface UserMapper {
	//Create
	@Insert("insert into user(name,username,password,email,level)"
			+ "values(#{name},#{username},#{password},#{email},#{level})")
	void addUser(User user);
	
	//Read
	@Select("select userId, name, username, password, email, level from user where username=#{username}")
	User getUserByUsername(String username);
	
	@Select("select userId, name, username, password, email, level from user where username=#{username} and level=2")
	User getUserByUsernameForManage(String username);
	
	
	//Update
	
	//Delete

}
