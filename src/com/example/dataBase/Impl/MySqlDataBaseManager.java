package com.example.dataBase.Impl;

import java.sql.Connection;
import java.sql.ResultSet;

import com.example.dataBase.DataBaseManager;

public class MySqlDataBaseManager implements DataBaseManager{
	
	private Connection mConnection = null;
	
	
	

	@Override
	public boolean ceareTable(String tableName, String[] fieldNames, String[] fieldTypes) {
	
		return false;
	}

	@Override
	public boolean insert(String tableName, String[] argNames, String[] argValues) {
		return false;
	}

	@Override
	public int update(String tableName, String[] selectArgNames, String[] selectArgValues, String[] updateArgNames,
			String[] updateArgValues) {
		return 0;
	}

	@Override
	public boolean delete(String tableName, String[] selectArgNames, String[] selectArgValues) {
		return false;
	}

	@Override
	public ResultSet query(String tableName, String[] columns, String[] selections, String[] selectionArgrs) {
		return null;
	}
	
	public void close() {
		
	}
}
