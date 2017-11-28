package com.example.dataBase;

import java.sql.ResultSet;

/**
 * 本接口主要是提供5个方法，用于数据库的基本操作，方法的详细解释请看com.example.dataBase.Impl.MySqlDataBaseManager类
 * @author apple
 *
 */
public interface DataBaseManager {
	
	
	public boolean ceareTable(String tableName, String[] fieldNames, String[] fieldTypes);
	
	public boolean insert(String tableName, String[] argNames, String[] argValues);
	
	public boolean update(String tableName, String[] selectArgNames, String [] selectArgValues, String [] updateArgNames, String [] updateArgValues);
	
	public boolean delete(String tableName, String [] selectArgNames, String []selectArgValues);
	
	public ResultSet query(String tableName, String [] selections, String []selectionValues);
	
	
}
