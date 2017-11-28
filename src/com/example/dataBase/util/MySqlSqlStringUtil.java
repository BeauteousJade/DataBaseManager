package com.example.dataBase.util;

public class MySqlSqlStringUtil {
	
	/**
	 * 本方法根据传递进来的条件，构建建表sql语句。其中tableName表示的是创建的表名，可以调用Bean的getClassName获取；
	 * columnNames表示的表中的字段名可以调用Bean的getFieldNames方法来获取；
	 * columnTypes表示的字段类型，可以调用Bean的getFieldTypes方法获取
	 * @param tableName 创建的表名
	 * @param columnNames 字段名数组
	 * @param columnTypes 字段类型数组
	 * @return String类型，成功构建合法的建表sql语句，返回sql；如果传递进来的条件不允许的话，返回为null
	 */
	public static String mergeCreateTableSql(String tableName, String[] columnNames, String []columnTypes) {
		if (tableName == null || columnNames == null || columnTypes == null || columnNames.length != columnTypes.length) {
			return null;
		}
		String sqlString = "create table " + tableName + "(";
		StringBuilder stringBuilder = new StringBuilder(sqlString);

		for (int i = 0; i < columnNames.length; i++) {
			stringBuilder.append(columnNames[i]);
			stringBuilder.append(" ");
			stringBuilder.append(columnTypes[i]);
			if (i != columnNames.length - 1) {
				stringBuilder.append(",");
			}
		}
		stringBuilder.append(")");
		return stringBuilder.toString();
	}
	/**
	 * 本方法构建查询的sql语句。其中tableName表示的是创建的表名，可以调用Bean的getClassName获取；
	 * args表示查询条件的字段名，可以手动设置，也可以通过调用Bean的getSignificativeFieldNames方法来获取；
	 * values表示是查询条件的字段值，可以手动设置，也可以通过调用Bean的getSignificativeFieldValues方法来获取。
	 * 由于考虑到sql注入的问题，所以在构建sql语句时，所有的字段都用?代替的，具体的赋值在MySqlDataBaseManager类中进行
	 * @param tableName 查询的表名
	 * @param args 查询条件的字段名
	 * @param values 查询条件的字段值
	 * @return String类型，成功构建合法的建表sql语句，返回sql；如果传递进来的条件不允许的话，返回为null
	 */
	public static String mergeQueryString(String tableName, String args[], String values[]) {
		if (tableName == null  || args == null || values == null) {
			return null;
		}
		if (args.length != values.length) {
			return null;
		}
		String sqlString = "select * from " + tableName;
		StringBuilder stringBuilder = new StringBuilder(sqlString);
		for (int i = 0; i < args.length; i++) {
			if (i == 0) {
				stringBuilder.append(" where ");
			}
			stringBuilder.append(args[i]);
			stringBuilder.append("=");
			stringBuilder.append("?");
			if (i != args.length - 1) {
				stringBuilder.append(" and ");
			}
		}
		return stringBuilder.toString();
	}
	/**
	 * 本方法构建更新sql语句。其中tableName表示的是更新的表名，可以通过调用Bean的getClassName方法来获取；
	 * selectArgs表示的是更新的条件字段名，必须设置为id，因为更新操作只能依靠id来进行
	 * selectValues表示的是更新的条件字段值，也就是id的值。
	 * updateArgs表示的是更新字段名称(除id之外)，如果用户设置了某些属性的值，那么那些属性名就是这里updateValues，可以通过调用
	 * Bean的getSignificativeFieldNames方法来获取
	 * updateValues表示的是更新字段的值（除id之外）可以通过调用getSignificativeFieldValues方法来获取
	 * @param tableName 更新的表名
	 * @param selectArgs 更新的条件字段名
	 * @param selectValues 更新的条件字段值
	 * @param updateArgs 更新字段名称(除id之外)
	 * @param updateValues 更新字段的值（除id之外）
	 * @return String类型，成功构建合法的建表sql语句，返回sql；如果传递进来的条件不允许的话，返回为null
	 */
	public static String mergeUpdateString(String tableName, String[] selectArgs, String[] selectValues,
			String[] updateArgs, String[] updateValues) {
		if (tableName == null || selectArgs == null || selectValues == null || updateArgs == null
				|| updateValues == null) {
			return null;
		}
		if (selectArgs.length != selectValues.length || updateArgs.length != updateValues.length) {
			return null;
		}
		String sqlString = "update " + tableName + " set ";
		StringBuilder stringBuilder = new StringBuilder(sqlString);

		for (int i = 0; i < selectArgs.length; i++) {
			stringBuilder.append(selectArgs[i]);
			stringBuilder.append("=");
			stringBuilder.append("?");
			if (i != selectArgs.length - 1) {
				stringBuilder.append(",");
			}
		}

		for (int i = 0; i < updateArgs.length; i++) {
			if (i == 0) {
				stringBuilder.append(" where ");
			}
			stringBuilder.append(updateArgs[i]);
			stringBuilder.append("=");
			stringBuilder.append("?");
			if (i != updateArgs.length - 1) {
				stringBuilder.append(" and ");
			}
		}
		return stringBuilder.toString();
	}
	/**
	 * 本方法用来构建插入sql语句。其中tableName表示的是插入的表名，可以通过调用Bean的getClassName方法来获取；
	 * args表示的是插入的字段名，必须是对象的所有属性名，可以通过调用Bean的getFieldNames方法来获取
	 * values表示的是插入的字段值，可以通过调用Bean的getFieldValues方法来获取
	 * @param tableName 插入的表名
	 * @param args 插入的字段名
	 * @param values 插入的字段值
	 * @return  String类型，成功构建合法的建表sql语句，返回sql；如果传递进来的条件不允许的话，返回为null
	 */
	public static String mergeInsertString(String tableName, String[] args, String[] values) {
		if (args.length != values.length) {
			return null;
		}
		String sqlString = "insert into " + tableName + " values(";
		StringBuilder stringBuilder = new StringBuilder(sqlString);
		for (int i = 0; i < args.length; i++) {	
			stringBuilder.append("?");
			if (i != args.length - 1) {
				stringBuilder.append(", ");
			}
		}
		stringBuilder.append(")");
		return stringBuilder.toString();
	}
	/**
	 * 本方法用来构建删除sql语句。其中tableName表示的是删除的表名，可以通过调用Bean的getClassName方法来获取；
	 * selectArgNames删除数据的条件名，必须为id，因为删除操作依靠id进行
	 * selectArgValues删除数据的条件值，也就是id的值
	 * @param tableName 删除的表名
	 * @param selectArgNames 删除数据的条件名
	 * @param selectArgValues 删除数据的条件值
	 * @return String类型，成功构建合法的建表sql语句，返回sql；如果传递进来的条件不允许的话，返回为null
	 */
	public static String mergeDeleteString(String tableName, String[] selectArgNames, String[] selectArgValues) {
		if (selectArgNames.length != selectArgValues.length) {
			return null;
		}
		String sqlString = "delete from " + tableName;
		StringBuilder stringBuilder = new StringBuilder(sqlString);
		for (int i = 0; i < selectArgNames.length; i++) {
			if (i == 0) {
				stringBuilder.append(" where ");
			}
			stringBuilder.append(selectArgNames[i]);
			stringBuilder.append("=");
			stringBuilder.append("?");
			if (i != selectArgNames.length - 1) {
				stringBuilder.append(" and ");
			}
		}
		return stringBuilder.toString();
	}
}
