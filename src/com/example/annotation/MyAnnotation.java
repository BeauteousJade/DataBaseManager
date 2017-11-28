package com.example.annotation;

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
