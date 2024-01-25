package com.example.demo.vo;

import lombok.Data;

/*
 * 	1. 使用者(User)
		(1) userId (101,102,...)
		(2) name
		(3) username
		(4) password
		(5) email
		(6) level: 1(一般會員),2(管理人員)
 */

@Data

public class User {
	private Integer userId;
	private String name;
	private String username;
	private String password;
	private String email;
	private Integer level;
	
	public User(String name, String username, String password, String email, Integer level) {
		this.name = name;
		this.username = username;
		this.password = password;
		this.email = email;
		this.level = level;
	}

	public User() {
	}

}
