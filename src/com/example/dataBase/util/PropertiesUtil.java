package com.example.dataBase.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

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
