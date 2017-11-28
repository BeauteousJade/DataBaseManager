package com.example.bean.baseBean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import com.example.annotation.MyAnnotation;

public abstract class BaseBean {
	
	@MyAnnotation(fieldMark = MyAnnotation.FieldMark.ID)
	private  String id = null;
	
	public abstract boolean save();
	public abstract boolean update();
	public abstract boolean delete();
	
	public String[] getFieldNames(){
		Field[] fields = this.getClass().getDeclaredFields();
		String[] fieldNames = new String[fields.length];
		for(int i = 0; i < fieldNames.length; i++) {
			fieldNames[i] = fields[i].getName();
		}
		return fieldNames;
	}
	public String[] getFieldValues() {
		String fieldNames[] = getFieldNames();
		String fieldValues[] = new String[fieldNames.length];
		for(int i = 0; i < fieldValues.length; i++) {
			String name = fieldNames[i].substring(0, 1).toUpperCase() + fieldNames[i].substring(1);
			try {
				fieldValues[i] = (String) this.getClass().getMethod("get" + name).invoke(this);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			if(fieldValues[i] == null) {
				fieldValues[i] = "";
			}
		}
		
		return fieldValues;
	}
	public String[] getFieldTypes(){
		Field[] fields = this.getClass().getDeclaredFields();
		String fieldTypes[] = new String[fields.length];
		for(int i = 0; i < fields.length; i++) {
			MyAnnotation annotation = fields[i].getAnnotation(MyAnnotation.class);
			if(annotation == null) {
				fieldTypes[i] = "varchar(200)";
				continue;
			}
            if(annotation.fieldType().equals(MyAnnotation.FieldType.LONG_STRING)) {
				fieldTypes[i] = "longtext";
			}else {
				fieldTypes[i] = "varchar(200)";
			}
				
		}
		return fieldTypes;
	}
	public String getClassName() {
		return this.getClass().getSimpleName();
	}
	
	public String getIdName() {
		Field[] fields = this.getClass().getFields();
		String idName = null;
		for(int i = 0; i < fields.length; i++) {
			MyAnnotation annotation = fields[i].getAnnotation(MyAnnotation.class);
			if(annotation.fieldMark().equals(MyAnnotation.FieldMark.ID)) {
				idName = fields[i].getName();
				break;
			}
		}
		return idName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
