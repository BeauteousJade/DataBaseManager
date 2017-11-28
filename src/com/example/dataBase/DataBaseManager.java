package com.example.dataBase;

import java.sql.ResultSet;

public interface DataBaseManager {
	
	
	public boolean ceareTable(String tableName, String[] fieldNames, String[] fieldTypes);
	
	public boolean insert(String tableName, String[] argNames, String[] argValues);
	
	public boolean update(String tableName, String[] selectArgNames, String [] selectArgValues, String [] updateArgNames, String [] updateArgValues);
	
	public boolean delete(String tableName, String [] selectArgNames, String []selectArgValues);
	
	public ResultSet query(String tableName, String[] columns, String [] selections, String []selectionValues);
	
	
}
