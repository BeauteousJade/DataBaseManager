package com.example.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation {
	
	public enum FieldType{
		NORMAL, LONG_STRING
	}
	public enum FieldMark{
		NORMAL, ID
	}
	
	public FieldType fieldType() default FieldType.NORMAL; 
	public FieldMark fieldMark() default FieldMark.NORMAL;
}
