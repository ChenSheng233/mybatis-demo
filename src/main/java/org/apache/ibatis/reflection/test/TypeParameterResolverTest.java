package org.apache.ibatis.reflection.test;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.reflection.TypeParameterResolver;

/**
 * TypeVariable	(类型变量，即泛型中的变量)
 * ParameterizedType	(参数化类型，即泛型)
 * GenericArrayType	(泛型数组类型，用来描述ParameterizedType、TypeVariable类型的数组；即List<T>[] 、T[]等)
 * Class
 * @author chensheng
 *
 */
public class TypeParameterResolverTest {
	
	public static void main(String[] args) throws NoSuchMethodException, SecurityException {
		Class<?> clazz = RestView.class;
		Method method = clazz.getDeclaredMethod("getData");
		Class<?> returnType = method.getReturnType();
		Type type =  method.getGenericReturnType();
		
		System.out.println(returnType);
		System.out.println(type);
		System.out.println();
		System.out.println();
		
		
		
		System.out.println("returnType instanceof Class:"+(returnType instanceof Class));
		System.out.println("type instanceof TypeVariable:"+(type instanceof TypeVariable));
		System.out.println("type instanceof Class:"+(type instanceof Class));
		System.out.println();
		System.out.println();
		
		clazz = Page.class;
		type = TypeParameterResolver.resolveReturnType(clazz.getDeclaredMethod("getRecords"),clazz);
//		clazz.getDeclaredMethod("getRecords");
		System.out.println("returnType instanceof Class:"+(returnType instanceof Class));
		System.out.println("type instanceof TypeVariable:"+(type instanceof TypeVariable));
		System.out.println("type instanceof Class:"+(type instanceof Class));
		System.out.println();
		System.out.println();
		
		RestView<String> result = new RestView<>();
		clazz = result.getClass();
		type = TypeParameterResolver.resolveReturnType(clazz.getDeclaredMethod("getData"),clazz);
		System.out.println(type);
		
		type = clazz.getDeclaredMethod("getData").getGenericReturnType();
		if(type instanceof TypeVariable) {
			TypeVariable<?> typeVariable = (TypeVariable<?>) type;
			Type[] bounds = typeVariable.getBounds();
			System.out.println(bounds[0]);
			System.out.println(type);
		}
		
		// 获取泛型参数类型要注意下面这些点：
		// 当一个方法返回值包含泛型或参数列表包含泛型。这个时候声明这些方法或参数值的接口和类可以是 ParameterizedType 或 Class
		// 同时，由于面向对象语言的继承特性，泛型方法或参数也可以被子类或接口继承。
		// 而 ParameterizedType的参数泛型类型 TypeVariable从另外一个角度，也可以是 ParameterizedType
		
		RestView<Page<String>> obj = new RestView<>();
		clazz = obj.getClass();
		method = clazz.getMethod("getData");
		type = TypeParameterResolver.resolveReturnType(method, clazz);
		System.out.println(type);
		
		RestView<String> obj1 = new RestView<>();
		clazz = obj1.getClass();
		method = clazz.getMethod("getData");
		type = TypeParameterResolver.resolveReturnType(method, clazz);
		System.out.println(type);
	}
	

	
	// 解析方法返回值类型
	private static void resolverReturnType(Class<?> clazz) {
		Method[] methods = getClassMethods(clazz);
		for(Method method:methods) {
			System.out.println(ReflectionToStringBuilder.toString(method,ToStringStyle.MULTI_LINE_STYLE));
			// 方法返回值
			Type type = TypeParameterResolver.resolveReturnType(method, clazz);
			System.out.println(type);
		}
	}

	public static Method[] getClassMethods(Class<?> clazz) {
		List<Method> result = new ArrayList<>();
		Class<?> currentClass = clazz;
//		isAssignableFrom
//		假设有两个类Class1和Class2。Class1.isAssignableFrom(Class2)表示:
//
//		类Class1和Class2是否相同。
//		Class1是否是Class2的父类或接口
//		调用者和参数都是java.lang.Class类型。
		while(currentClass !=null && !currentClass.isAssignableFrom(Object.class)) {
			
			addMethods(result,currentClass.getDeclaredMethods());
			
			Arrays.stream(currentClass.getInterfaces()).forEach(_interface -> addMethods(result,_interface.getDeclaredMethods()));
			
			currentClass = currentClass.getSuperclass();
		}
		return result.toArray(new Method[0]);
	}
	
	private static void addMethods(List<Method> list,Method[] methods) {
		Arrays.stream(methods).filter(method -> !method.isBridge()).forEach(method -> { System.out.println(method.getName());list.add(method);});
	}
	
	public static Type resolveReturnType(Method method) {
		return  method.getReturnType();
//		return null;
	}


	static class Page<T>{
		private Long pageNumber;
		private Long pageSize;
		private Long total;
		private List<T> records;
		public Long getPageNumber() {
			return pageNumber;
		}
		public void setPageNumber(Long pageNumber) {
			this.pageNumber = pageNumber;
		}
		public Long getPageSize() {
			return pageSize;
		}
		public void setPageSize(Long pageSize) {
			this.pageSize = pageSize;
		}
		public Long getTotal() {
			return total;
		}
		public void setTotal(Long total) {
			this.total = total;
		}
		public List<T> getRecords() {
			return records;
		}
		public void setRecords(List<T> records) {
			this.records = records;
		}
		
	}
	static class RestView<T>{
		private Integer code;
		private T data;
		private String message;
		private Long timeStamp;
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		public T getData() {
			return data;
		}
		public void setData(T data) {
			this.data = data;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public Long getTimeStamp() {
			return timeStamp;
		}
		public void setTimeStamp(Long timeStamp) {
			this.timeStamp = timeStamp;
		}
		
	}
}
