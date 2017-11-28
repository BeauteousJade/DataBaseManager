package com.example.bean;

import com.example.annotation.MyAnnotation;
import com.example.bean.baseBean.BaseBean;

/**
 * 本类用于一个案例，不做过多的解释
 * @author apple
 *
 */
public class User extends BaseBean {
	@MyAnnotation(fieldType = MyAnnotation.FieldType.LONG_STRING)
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
}
