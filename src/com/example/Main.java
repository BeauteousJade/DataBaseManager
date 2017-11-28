package com.example;

import java.util.List;

import com.example.bean.User;
import com.example.dataBase.operation.MySqlOperation;

public class Main {
	public static void main(String []args) {
		MySqlOperation<User> operation = new MySqlOperation<>();
		
		User user = new User();
		
		//保存一条记录，对应的表名是类名，如果表不存在的话，会自定创建
		operation.save(user);
		
		//删除一条记录，删除记录时必须调用setId方法类设置
		user.setId("id");
		operation.delete(user);
		
		//更新一条记录，设置你需要改变的属性的值，必须设置Id
		user.setId("id");
		user.setPassword("password");
		user.setDisplayName("displayName");
		operation.update(user);
		
		//查询记录，两种方式
		//通过Id，只要找到一条记录
		user.setId("id");
		//找到返回一个User的List，其中的每个对象属性都是与数据库一致
		List<User> userList1 = operation.queryById(user);
		
		//通过有值的属性，可能会找到很多的记录
		user.setUserName("pby");
		List<User> userList2 = operation.queryById(user);
		
		//关闭连接
		operation.close();
	}
}
