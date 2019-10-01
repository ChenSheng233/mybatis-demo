package org.apache.ibatis.reflection.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.reflection.Reflector;

public class ReflectorTest {
	
	static class NullObject{
		public int code = -1;

		@Override
		public String toString() {
			return "NullObject [code=" + code + "]";
		}
		
	}
	static Constructor<?> defaultConstructor = null;
	public static void main(String[] args) {
		Class<?> clazz = DataSource.class;
		
		Class<?> type = clazz;
		
		System.out.println("Class<T> 实例方法获取所有构造器：getDeclaredConstructors():Constructor<?>[]");
		Constructor<?>[] constructors = clazz.getDeclaredConstructors();
		System.out.println(constructors);
		
		
		Method[] declaredMethods =clazz.getDeclaredMethods();
		List<Method> list = Arrays.asList(declaredMethods);
		List<Method> declaredMethodList = Arrays.stream(clazz.getMethods()).filter(method -> list.contains(method)).collect(Collectors.toList());
		
		Method[] methods = getMethods(DataSource.class);
		Arrays.stream(methods).forEach(method -> System.out.println(ReflectionToStringBuilder.toString(method,ToStringStyle.MULTI_LINE_STYLE)));

		
		
		methods = getClassMethods(DataSource.class);
		Arrays.stream(methods).forEach(method -> System.out.println(ReflectionToStringBuilder.toString(method,ToStringStyle.MULTI_LINE_STYLE)));
		
		Arrays.stream(constructors).filter(constructor -> constructor.getParameters().length == 0)
			.findAny().ifPresent(constructor -> defaultConstructor = constructor);
		System.out.println("获取默认构造器:"+defaultConstructor);
		

		
		Reflector  reflectorDataSource = new Reflector(DataSource.class);
		boolean hasPasswordGetProperty = reflectorDataSource.hasGetter("password");
		
	}
	public static Method[] getClassMethods(Class<?> clazz) {
		
		 return clazz.getMethods();
	}
	
	public static Method[] getMethods(Class<?> clazz) {
		List<Method> result = new ArrayList<>();
		
		Class<?> currentClass = clazz;
		while(currentClass!=null&& Object.class.isAssignableFrom(clazz)) {

			addMethods(result,currentClass.getDeclaredMethods());
			
			Class<?>[] interfaces = currentClass.getInterfaces();
			Arrays.stream(interfaces).forEach(_interface -> result.addAll(Arrays.asList(getMethods(_interface))));
			
			currentClass = currentClass.getSuperclass();
		}
		return result.toArray(new Method[] {});
	}
	
	private static void addMethods(List<Method> list,Method[] methods) {
		for(Method method:methods) {
			if(!method.isBridge()) {
				list.add(method);
			}
		}
	}
	
	public static void base() {
		List<String> list = new ArrayList<>();
		addList(list);
		System.out.println(list);

		String str= "Time";
		System.out.println(str);
		changeStr(str);
		System.out.println(str);
		
		int code = 110;
		System.out.println(code);
		changeInt(code);
		System.out.println(code);
		
		Integer codeObj = 110;
		changeInt(codeObj);
		System.out.println(codeObj);
		
		
		NullObject obj = new NullObject();
		obj.code = 110;
		System.out.println(obj);
		changeObj(obj);
		System.out.println(obj);
		
		Map<String,Object> strList = new HashMap<>();
		NullObject element = (NullObject) strList.computeIfAbsent("name", item -> new NullObject());
		 System.out.println(strList);
		 element.code = 110;
		 System.out.println(strList);
		 Integer num = -1;
		 Integer result = (Integer) strList.computeIfAbsent("name2", item -> num);
		 System.out.println(strList);
		 result = 110;
		 System.out.println(strList);
	}
	
	public static void addList(List<String> list) {
		list.add("Local");
	}
	
	public static void changeStr(String str) {
		str = "Date Time";
	}
	
	public static void changeInt(Integer code) {
		code = 120;
	}
	public static void changeObj(NullObject obj) {
		obj.code = 120;
	}
}
