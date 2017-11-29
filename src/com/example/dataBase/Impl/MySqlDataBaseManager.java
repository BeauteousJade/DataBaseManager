package com.example.dataBase.Impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.dataBase.DataBaseManager;
import com.example.dataBase.util.MySqlSqlStringUtil;
import com.example.dataBase.util.PropertiesUtil;
/**
 * 
 * 本类实现了DataBaseManager接口，并且实现了该接口的5个方法。本类是真正操作数据类的，其他的类都是都是本类的代理。
 * 本类中定义了数据库的操作方式，包括增、删、改、查
 * @author apple
 *
 */
public class MySqlDataBaseManager implements DataBaseManager {

	//Connection对象，用于数据的连接
	private Connection mConnection = null;

	/**
	 * 构造方法，这个方法主要创建了Connection的对象，用于对数据库的访问
	 */
	public MySqlDataBaseManager() {
		//通过调用ProertiesUtil方法从dataBase.properties文件中分别获取url，userName，password，driver的值
		String []values = PropertiesUtil.getValues("dataBase.properties", "url", "userName", "password", "driver");
		try {
			Class.forName(values[3]);
			mConnection = DriverManager.getConnection(values[0], values[1], values[2]);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 本方法用于在数据库中创建一张对应的表，其中tableName 表示的是表名，可以通过调用Bean的getClassName方法来获取
	 * fieldNames表示的是属性名，对着表中的字段名，可以通过调用Bean的getFieldNames方法来获取
	 * fieldTypes 表示的是属性名类型，对应着表中的字段类型，可以通过调用Bean的getFieldTypes方法来获取。
	 * 如果创建表失败，返回的false，反之返回true
	 * @param tableName  类名，对应着表名
	 * @param fieldNames 属性名数组，对应着字段名
	 * @param fieldValues 属性类型数组，对应着字段类型
	 * @return boolean值，true表示创建表成功，false表示创建表失败
	 */
	@Override
	public boolean ceareTable(String tableName, String[] fieldNames, String[] fieldTypes) {
		String sqlString = MySqlSqlStringUtil.mergeCreateTableSql(tableName, fieldNames, fieldTypes);
		if(sqlString == null) {
			return false;
		}
		try(PreparedStatement prepareStatement = mConnection.prepareStatement(sqlString)) {
			return !prepareStatement.execute();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 本方法用于对数据库进行插入操作。其中tableName表示的是表名，可以通过调用Bean的getClass方法来获取；
	 * argNames表示的是属性名，对着表中的字段名，可以通过调用Bean的getFieldNames方法来获取；
	 * argValues 表示的是属性名的值，对应着表中的字段值，可以通过调用Bean的getFieldValues方法来获取。
	 * 如果插入数据失败，返回的false，反之返回true
	 * @param tableName  类名，对应着表名
	 * @param argNames 属性名数组，对应着字段名
	 * @param argValues 属性值数组，对应着字段值
	 * @return boolean值，true表示插入成功，false表示插入失败 
	 */
	@Override
	public boolean insert(String tableName, String[] argNames, String[] argValues) {
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
	
	/**
	 * 本方法用于对数据的更新操作。其中tableName表示的是表名，可以通过调用Bean的getClass方法来获取；
	 * selectArgNames表示的是更新的条件属性名，通过手动设置；
	 * selectArgValues表示的是更新的条件属性值，通过手动设置；
	 * updateArgNames表示的是更新的字段名，可以通过调用Bean的getSignificativeFieldNames来获取；
	 * updateArgValues表示的是更新的字段值，可以通过调用Bean的getSignificativeFieldValues方法来获取。
	 * 如果更新失败的话，返回的是false，否则返回true。
	 * @param selectArgNames 表示的是更新的条件名
	 * @param selectArgValues 表示的是更新的条件值
	 * @param updateArgNames 表示的是更新的字段名
	 * @param updateArgValues 表示的是更新的字段值
	 * @return boolean值，true表示的是更新数据成功，false表示的是更新数据失败
	 */
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
	
	/**
	 * 本方法主要是用于数据的删除，需要注意的是，本方法仅支持id主键删除，其他方式的删除会被从代理那里禁止的。
	 * 其中tableName表示的是表名，可以通过调用Bean类的getClassName方法来获取；
	 * argNames表示的是删除数据的条件名，虽然这里是数组，但是从代理那里传递过来的只有id属性名；
	 * argValues表示的是删除数据的条件值，虽然这里是数组，但是从代理那里传递过来的只有id的值；
	 * 如果删除失败的话，返回的false，反之返回的是true。
	 * @param tableName 表示的是表名
	 * @param argNames 删除数据的条件名，实际上这个数组只有一个idName
	 * @param argValues 删除数据的条件值，实际上这个数组只有一个id值
	 * @return boolean类型的值，true表示删除成功，false表示删除失败
	 */
	@Override
	public boolean delete(String tableName, String[] argNames, String[] argValues) {
		String sqlString = MySqlSqlStringUtil.mergeDeleteString(tableName, argNames, argValues);
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
	
	/**
	 * 本方法用对数据的查询。其中tableName表示的是表名，可以通过调用Bean的getClassName方法来获取；
	 * selections表示的是查询的条件名，可以通过手动设置，也可以调用Bean的getSignificativeFieldNames方法来获取非空的属性名；
	 * selectionValues表示的是查询的属性值，可以通过手动设置，也可以调用Bean的getSignificativeFieldValues方法来获取非空的属性值；
	 * 查询成功的话返回的是ResultSet的对象，查询失败返回的是null。
	 * @param tableName 表示的是表名
	 * @param selections 表示的是查询的条件名
	 * @param selectionValues 表示的是查询的条件值
	 * @return 如果查询成功的话，返回的是ResultSet的对象(这个对象有可能没有值)，查询失败的话，返回null
	 */
	@Override
	public ResultSet query(String tableName, String[] selections, String[] selectionValues) {

		String sqlString = MySqlSqlStringUtil.mergeQueryString(tableName, selections, selectionValues);
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
	
	/**
	 * 关闭数据库的连接
	 */
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
