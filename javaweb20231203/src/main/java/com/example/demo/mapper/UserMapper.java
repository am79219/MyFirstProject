package com.example.demo.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.model.vo.User;

@Mapper

public interface UserMapper {
	//Create
	@Insert("insert into user(name,username,password,email,level)"
			+ "values(#{name},#{username},#{password},#{email},#{level})")
	void addUser(User user);
	
	//Read
	@Select("select userId, name, username, password, email, level from user where username=#{username}")
	Optional<User> getUserByUsername(String username);
	
	@Select("select userId, name, username, password, email, level from user where username=#{username} and level=2")
	Optional<User> getUserByUsernameForManage(String username);
	
	@Select("select userId, name, username, password, email, level from user where userId=#{userId}")
	Optional<User> getUserById(Integer userId);
	
	//Update
	@Update("update user set name=#{name}, password=#{password}, email=#{eamil} where userId=#{userId}")
	void updateUserInfoById(User user, Integer userId);
	
	//Delete
	@Delete("delete from user where userId=#{userId}")
	void deleteUserById(Integer userId);
	

}
