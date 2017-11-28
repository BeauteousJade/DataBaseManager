package com.example;

import com.example.bean.User;

public class Main {
	public static void main(String []args) {
		User user = new User();
		user.setPassword("pby");
		user.setUserName("pby");
		user.save();
	}
}
