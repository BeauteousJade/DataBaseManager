package com.example.bean.baseBean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.annotation.MyAnnotation;

public abstract class BaseBean {

	@MyAnnotation(fieldMark = MyAnnotation.FieldMark.ID)
	private  String id = null;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String[] getFieldNames() {
		List<Field> fieldList = getAllField();
		String[] fieldNames = new String[fieldList.size()];
		for (int i = 0; i < fieldNames.length; i++) {
			fieldNames[i] = fieldList.get(i).getName();
		}
		return fieldNames;
	}

	public String[] getUpdateFieldNames() {
		List<Field> fieldList = getAllField();
		String[] fieldValues = getFieldValues();
		List<String> fieldNameList = new ArrayList<>();
		for (int i = 0; i < fieldList.size(); i++) {
			MyAnnotation annotation = fieldList.get(i).getAnnotation(MyAnnotation.class);
			if ((annotation != null && annotation.fieldMark().equals(MyAnnotation.FieldMark.ID)) || fieldValues[i].equals("")) {
				continue;
			}
			fieldNameList.add(fieldList.get(i).getName());
		}
		return fieldNameList.toArray(new String[] {});
	}

	public String[] getUpdateFieldValues() {
		List<Field> fieldList = getAllField();
		String[] fieldValues = getFieldValues();

		List<String> fieldValueList = new ArrayList<>();
		for (int i = 0; i < fieldList.size(); i++) {
			MyAnnotation annotation = fieldList.get(i).getAnnotation(MyAnnotation.class);
			if ((annotation != null && annotation.fieldMark().equals(MyAnnotation.FieldMark.ID)) || fieldValues[i].equals("")) {
				continue;
			}
			fieldValueList.add(fieldValues[i]);
		}
		return fieldValueList.toArray(new String[] {});

	}

	public String[] getFieldValues() {
		String fieldNames[] = getFieldNames();
		String fieldValues[] = new String[fieldNames.length];
		for (int i = 0; i < fieldValues.length; i++) {
			String name = fieldNames[i].substring(0, 1).toUpperCase() + fieldNames[i].substring(1);
			try {
				fieldValues[i] = (String) this.getClass().getMethod("get" + name).invoke(this);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			if (fieldValues[i] == null) {
				fieldValues[i] = "";
			}
		}

		return fieldValues;
	}

	public String[] getFieldTypes() {
		List<Field> fieldList = getAllField();
		String fieldTypes[] = new String[fieldList.size()];
		for (int i = 0; i < fieldList.size(); i++) {
			MyAnnotation annotation = fieldList.get(i).getAnnotation(MyAnnotation.class);
			if (annotation == null) {
				fieldTypes[i] = "varchar(200)";
				continue;
			}
			if (annotation.fieldType().equals(MyAnnotation.FieldType.LONG_STRING)) {
				fieldTypes[i] = "longtext";
			} else {
				fieldTypes[i] = "varchar(200)";
			}
			if (annotation.fieldMark().equals(MyAnnotation.FieldMark.ID)) {
				fieldTypes[i] += " primary key";
			}
		}
		return fieldTypes;
	}

	public String getClassName() {
		return this.getClass().getSimpleName();
	}

	public String getIdName() {
		List<Field> fieldList = getAllField();
		String idName = null;
		for (int i = 0; i < fieldList.size(); i++) {
			MyAnnotation annotation = fieldList.get(i).getAnnotation(MyAnnotation.class);
			if (annotation.fieldMark().equals(MyAnnotation.FieldMark.ID)) {
				idName = fieldList.get(i).getName();
				break;
			}
		}
		return idName;
	}

	private List<Field> getAllField() {
		List<Field> fieldList = new ArrayList<>();
		Class<?> clazz = this.getClass();
		while (clazz != null) {
			fieldList.addAll(0, Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		}
		return fieldList;
	}
	public   Object makeObject(String[] fields, String[] values) {
		for (int i = 0; i < fields.length; i++) {
			String name = fields[i].substring(0, 1).toUpperCase() + fields[i].substring(1);
			try {
				this.getClass().getMethod("set" + name, String.class).invoke(this, values[i]);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
				return null;
			}
		}
		return this;
	}
}
