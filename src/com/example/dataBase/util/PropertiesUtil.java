package com.example.dataBase.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
	/**
	 * 本方法用来获取properties文件特定name的值，其中需要传递properties文件名(propertyFileName),需要获取特定name的值
	 * @param propertyFileName properties文件名
	 * @param name 需要获取的值对应的name值
	 * @return String类型，获取的值，如果获取失败，或者没有这个name这个值，返回为null，反之返回name对应的值
	 */
	public static String getValue(String propertyFileName, String name) {
		Properties properties = new Properties();
		try (FileInputStream fis = new FileInputStream("dataBase.properties");) {
			properties.load(fis);
			return properties.getProperty(name);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 本方法是getValue方法复数形式，可以传递多个name值，获取多个值
	 * @param propertyFileName properties文件名
	 * @param names 需要获取的值对应的name值（复数形式）
	 * @return  String[]类型，获取的值，如果获取失败，或者没有这个name这个值，返回为null，反之返回names对应的值
	 */
	public static String[] getValues(String propertyFileName, String... names) {
		Properties properties = new Properties();
		try (FileInputStream fis = new FileInputStream("dataBase.properties");) {
			properties.load(fis);
			String values[] = new String[names.length];
			for (int i = 0; i < values.length; i++) {
				values[i] = properties.getProperty(names[i]);
			}
			return values;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
