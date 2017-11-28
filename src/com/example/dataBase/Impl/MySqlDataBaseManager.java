package com.example.dataBase.Impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.example.dataBase.DataBaseManager;
import com.example.dataBase.util.MySqlSqlStringUtil;
import com.example.dataBase.util.PropertiesUtil;

public class MySqlDataBaseManager implements DataBaseManager {

	private Connection mConnection = null;

	public MySqlDataBaseManager() {
		Properties properties = new Properties();
		try (FileInputStream fis = new FileInputStream("dataBase.properties");) {
			properties.load(fis);
			String []values = PropertiesUtil.getValues("dataBase.properties", "url", "userName", "password", "driver");
			Class.forName(values[3]);
			mConnection = DriverManager.getConnection(values[0], values[1], values[2]);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean ceareTable(String tableName, String[] fieldNames, String[] fieldTypes) {
		String sqlString = MySqlSqlStringUtil.mergeCreateTableSql(tableName, fieldNames, fieldTypes);
		if(sqlString == null) {
			return false;
		}
		try(PreparedStatement prepareStatement = mConnection.prepareStatement(sqlString)) {
			System.out.println(prepareStatement.toString());
			return !prepareStatement.execute();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public boolean insert(String tableName, String[] argNames, String[] argValues) {
		String sqlString = MySqlSqlStringUtil.mergeInsertString(tableName, argNames, argValues);
		System.out.println(sqlString);
		if (sqlString == null) {
			return false;
		}
		try (PreparedStatement prepareStatement = mConnection.prepareStatement(sqlString);) {
			for (int i = 0; i < argValues.length; i++) {
				prepareStatement.setString(i + 1, argValues[i]);
			}
			return prepareStatement.executeUpdate() != 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean update(String tableName, String[] selectArgNames, String[] selectArgValues, String[] updateArgNames,
			String[] updateArgValues) {
		String sqlString = MySqlSqlStringUtil.mergeUpdateString(tableName, selectArgNames, selectArgValues,
				updateArgNames, updateArgValues);
		try (PreparedStatement prepareStatement = mConnection.prepareStatement(sqlString)) {
			for (int i = 0; i < selectArgValues.length + updateArgValues.length; i++) {
				if (i < selectArgValues.length) {
					prepareStatement.setString(i + 1, selectArgValues[i]);
				} else {
					prepareStatement.setString(i + 1, updateArgValues[i - selectArgValues.length]);
				}
			}
			return prepareStatement.executeUpdate() != 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(String tableName, String[] argNames, String[] argValues) {
		String sqlString = MySqlSqlStringUtil.mergeInsertString(tableName, argNames, argValues);
		if (sqlString == null) {
			return false;
		}
		try (PreparedStatement prepareStatement = mConnection.prepareStatement(sqlString);) {
			for (int i = 0; i < argValues.length; i++) {
				prepareStatement.setString(i + 1, argValues[i]);
			}
			return prepareStatement.executeUpdate() != 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public ResultSet query(String tableName, String[] columns, String[] selections, String[] selectionValues) {

		String sqlString = MySqlSqlStringUtil.mergeQueryString(tableName, columns, selections, selectionValues);
		if (sqlString == null) {
			return null;
		}
		try {
			PreparedStatement prepareStatement = mConnection.prepareStatement(sqlString);
			for (int i = 0; i < selectionValues.length; i++) {
				prepareStatement.setString(i + 1, selectionValues[i]);
			}
			return prepareStatement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void close() {
		try {
			if (mConnection != null && !mConnection.isClosed()) {
				mConnection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
