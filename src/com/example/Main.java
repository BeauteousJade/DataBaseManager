package com.example;

import java.util.Arrays;
import java.util.List;

import com.example.bean.User;
import com.example.dataBase.operation.MySqlOperation;

public class Main {
	public static void main(String []args) {
		User user = new User();
		user.setId("e32f203c-fa37-431b-9e47-0efb5d77be6e");
		MySqlOperation<User> operation = new MySqlOperation<>();
		List<User> list = operation.query(user);
		System.out.println(Arrays.toString(list.get(0).getFieldValues()));
		operation.close();
	}
}
