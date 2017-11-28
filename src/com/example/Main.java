package com.example;

import java.util.List;

import com.example.bean.User;
import com.example.dataBase.operation.MySqlOperation;

public class Main {
	public static void main(String []args) {
		MySqlOperation<User> operation = new MySqlOperation<>();
		User user = new User();
		user.setUserName("pby");
//		for(int i = 0; i < 10; i++) {
//			user.setDisplayName("displayName" + i);
//			user.setPassword("passsword" + i);
//			operation.save(user);
//		}
		List<User> list = operation.queryByValuedField(user);
		System.out.println(list.size());
		operation.close();
	}
}
