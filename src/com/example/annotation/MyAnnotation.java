package com.example.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation {
	
	/**
	 * Bean类中的属性的类型，分为普通字符串类型和大字符串类型
	 * NORMAL--普通字符串类型
	 * LONG_STRING 大字符串类型
	 * @author apple
	 *
	 */
	public enum FieldType{
		NORMAL, LONG_STRING
	}
	/**
	 * Bean类中的属性标记，如果使用了ID标记，表示是数据库中的主键
	 * @author apple
	 *
	 */
	public enum FieldMark{
		NORMAL, ID
	}
	
	public FieldType fieldType() default FieldType.NORMAL; 
	public FieldMark fieldMark() default FieldMark.NORMAL;
}
