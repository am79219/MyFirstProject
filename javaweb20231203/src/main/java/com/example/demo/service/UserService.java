package com.example.demo.service;


public interface UserService {
	boolean loginInfoConfirm(String username,String password);
	boolean loginInfoConfirmForManage(String username,String password);
}
