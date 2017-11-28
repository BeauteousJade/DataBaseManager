package com.example.dataBase.util;

public class MySqlSqlStringUtil {
	
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
	public static String mergeQueryString(String tableName, String selectArgs[], String args[], String values[]) {
		if (tableName == null || selectArgs == null || args == null || values == null) {
			return null;
		}
		if (args.length != values.length) {
			return null;
		}
		String sqlString = "select ";
		StringBuilder stringBuilder = new StringBuilder(sqlString);
		for (int i = 0; i < selectArgs.length; i++) {
			stringBuilder.append(selectArgs[i]);
			if (i != selectArgs.length - 1) {
				stringBuilder.append(",");
			}
		}
		if (selectArgs.length == 0) {
			stringBuilder.append("* ");
		}
		stringBuilder.append(" from " + tableName);
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
		System.out.println(stringBuilder.toString());
		return stringBuilder.toString();
	}
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
			if (i != selectArgs.length - 1) {
				stringBuilder.append(" and ");
			}
		}
		return stringBuilder.toString();
	}
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
