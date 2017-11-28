package com.example.bean;

import java.util.UUID;

import com.example.bean.baseBean.BaseBean;
import com.example.dataBase.operation.MySqlOperation;
import com.example.exception.IdIsNullException;
import com.example.exception.IdNotExistException;

public class User extends BaseBean{
	private String userName;
	private String password;
	private String displayName;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	@Override
	public boolean save() {
		MySqlOperation<User> operation = new MySqlOperation<>();
		
		this.setId(UUID.randomUUID().toString());
		boolean isSuccess = operation.insert(this);
		operation.close();
		return isSuccess;
	}
	@Override
	public boolean update() {
		if(this.getId() == null) {
			throw new IdIsNullException();
		}
		if(this.getIdName() == null) {
			throw new IdNotExistException();
		}
		MySqlOperation<User> operation = new MySqlOperation<>();
		boolean isSuccess = operation.update(this);
		operation.close();
		return isSuccess;
	}
	@Override
	public boolean delete() {
		if(this.getId() == null) {
			throw new IdIsNullException();
		}
		if(this.getIdName() == null) {
			throw new IdNotExistException();
		}
		MySqlOperation<User> operation = new MySqlOperation<>();
		boolean isSuccess = operation.delete(this);
		operation.close();
		return isSuccess;
	}
	
}
