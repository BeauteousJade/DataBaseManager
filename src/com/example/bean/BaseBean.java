package com.example.bean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.annotation.MyAnnotation;

/**
 * 本类是用来供其他Bean类继承的，通过继承本类的Bean类，可以保存在数据库中的一张表，一个类对应一张表
 * 
 * @author apple
 *
 */
public abstract class BaseBean {

	/**
	 * id主键，所有的Bean类的主键，因此在自定义的Bean类中不不需要定义di主键。
	 * 如果定义了的话，会出现错误的。同时自定义的属性不能使用@MyAnnotation(fieldMark =
	 * MyAnnotation.FieldMark.ID) 注解，如果定义了本注解的话，对应的数据库表会出现两个主键。
	 * 
	 * 对于大佬来说，可以根据自己的需要来改写代码！
	 * 
	 */
	@MyAnnotation(fieldMark = MyAnnotation.FieldMark.ID)
	private String id = null;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 本方法用来获取一个类中所有的属性名，包括父类的所有属性，private，protected，default，public都在内
	 * 
	 * @return 返回的获取的属性名数组
	 */
	public String[] getFieldNames() {
		List<Field> fieldList = getAllField();
		String[] fieldNames = new String[fieldList.size()];
		for (int i = 0; i < fieldNames.length; i++) {
			fieldNames[i] = fieldList.get(i).getName();
		}
		return fieldNames;
	}

	/**
	 * 本方法获取一个类中的所有属性值，包括父类的所有属性，private，protected，default，public都在内
	 * 
	 * @return 返回获取的属性值数组
	 */
	public String[] getFieldValues() {
		String fieldNames[] = getFieldNames();
		String fieldValues[] = new String[fieldNames.length];
		for (int i = 0; i < fieldValues.length; i++) {
			// 将一个属性名格式化为需要的格式，例如，一个属性名：userName，通过本步骤，变为UserName
			String name = fieldNames[i].substring(0, 1).toUpperCase() + fieldNames[i].substring(1);
			try {
				// 调用属性相应的get方法，获取属性值。例如一个名为userName的属性，get方法为getUserName
				// 这里也是属性名必须使用驼峰命名法，并且必须含有get方法的原因
				fieldValues[i] = (String) this.getClass().getMethod("get" + name).invoke(this);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			// 如果属性值为空的话，自动变为空字符串
			if (fieldValues[i] == null) {
				fieldValues[i] = "";
			}
		}
		return fieldValues;
	}

	/**
	 * 本方法获取一个类中的所有非空的属性名(不包括id主键)，包括父类的所有属性，private，protected，default，public都在内
	 * 
	 * @return 返回获取的属性名数组
	 */
	public String[] getSignificativeFieldNames() {
		List<Field> fieldList = getAllField();
		String[] fieldValues = getFieldValues();
		List<String> fieldNameList = new ArrayList<>();
		for (int i = 0; i < fieldList.size(); i++) {
			MyAnnotation annotation = fieldList.get(i).getAnnotation(MyAnnotation.class);
			// 如果一个属性的注解为ID标记，或者属性值为空，表示不符合要求
			if ((annotation != null && annotation.fieldMark().equals(MyAnnotation.FieldMark.ID))
					|| fieldValues[i].equals("")) {
				continue;
			}
			fieldNameList.add(fieldList.get(i).getName());
		}
		return fieldNameList.toArray(new String[] {});
	}

	/**
	 * 本方法获取一个类中非空的属性名的值(不包括id)，包括父类的所有属性，private，protected，default，public都在内
	 * 
	 * @return 返回获取的属性值数组
	 */
	public String[] getSignificativeFieldValues() {
		List<Field> fieldList = getAllField();
		String[] fieldValues = getFieldValues();

		List<String> fieldValueList = new ArrayList<>();
		for (int i = 0; i < fieldList.size(); i++) {
			MyAnnotation annotation = fieldList.get(i).getAnnotation(MyAnnotation.class);
			// 如果一个属性的注解为ID标记，或者属性值为空，表示不符合要求
			if ((annotation != null && annotation.fieldMark().equals(MyAnnotation.FieldMark.ID))
					|| fieldValues[i].equals("")) {
				continue;
			}
			fieldValueList.add(fieldValues[i]);
		}
		return fieldValueList.toArray(new String[] {});

	}

	/**
	 * 本方法获取所有属性的类型，包括父类的所有属性。需要注意的是，这里需要判断每个属性的注解，如果一个属性FieldType为LONG_STRING的话，
	 * 表示该属性在数据库中是lontext类型，如果一个实行的FieldMark为ID，表示该属性在数据库中是主键
	 * 
	 * @return 返回获取的类型数组
	 */
	public String[] getFieldTypes() {
		List<Field> fieldList = getAllField();
		String fieldTypes[] = new String[fieldList.size()];
		for (int i = 0; i < fieldList.size(); i++) {
			MyAnnotation annotation = fieldList.get(i).getAnnotation(MyAnnotation.class);
			// 如果属性的注解为空，为varchar(200)
			if (annotation == null) {
				fieldTypes[i] = "varchar(200)";
				continue;
			}
			// 如果FieldTyp为LONG_STRING，为longtext
			if (annotation.fieldType().equals(MyAnnotation.FieldType.LONG_STRING)) {
				fieldTypes[i] = "longtext";
			} else { // 默认为varchar(200)
				fieldTypes[i] = "varchar(200)";
			}
			// 如果一个属性的FieldMark是ID，为主键
			if (annotation.fieldMark().equals(MyAnnotation.FieldMark.ID)) {
				fieldTypes[i] += " primary key";
			}
		}
		return fieldTypes;
	}

	/**
	 * 获取该类的类名
	 * 
	 * @return 返回类名
	 */
	public String getClassName() {
		return this.getClass().getSimpleName();
	}

	/**
	 * 获取该类的Id主键的属性名，本方法是通过@MyAnnotation(fieldMark = MyAnnotation.FieldMark.ID)
	 * 注解来获取的id名的，如果一个属性带有该注解，表示为id主键，否则就不是
	 * 
	 * @return 返回id主键名
	 */
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

	/**
	 * 本方法获取该类的所有属性(Field类型)，包括父类的所有属性，private，protected，default，public都在内
	 * 
	 * @return
	 */
	private List<Field> getAllField() {
		List<Field> fieldList = new ArrayList<>();
		Class<?> clazz = this.getClass();
		while (clazz != null) {
			fieldList.addAll(0, Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		}
		return fieldList;
	}

	/**
	 * 本方法来构造一个该类的对象的。通过设置传递进来的属性名和属性值，构造一个对象。如果构造失败，返回null
	 * 
	 * @param fields
	 *            该类对象对象需要设置的属性名数组
	 * @param values
	 *            该类对象需要设置的属性值数组
	 * @return 返回一个Object类型的引用，实际上是该类的对象
	 */
	public Object makeObject(String[] fields, String[] values) {
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
