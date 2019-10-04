package com.juku.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.apache.ibatis.reflection.Reflector;
import org.apache.ibatis.reflection.TypeParameterResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.juku.reflection.TypeResolverTest.DefaultRestView;

@RunWith(JUnit4.class)
public class TypeResolverTest {

//	public 
	@Test
	public void test1() throws NoSuchMethodException, SecurityException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> clazz = RestView.class;
		Method method = clazz.getDeclaredMethod("getCode");
		Type type = TypeResolver.resolveReturnType(method, clazz);
		System.out.println(type);
		
		if(type instanceof Class) {
			clazz = (Class<?>) type;
			int t = (int) clazz.cast(123);
			System.out.println(t);
		}
		
	}
	

	
	@Test
	public void test4() throws NoSuchMethodException, SecurityException {
		Class<?> clazz = DefaultRestView.class;
		
		Type type = clazz;
		while(type!=null) {
			if(type instanceof TypeVariable) {
				
			}else if(type instanceof ParameterizedType) {
				// 
			}
			else if(type instanceof Class) {
				clazz = (Class<?>)type;
				type = clazz.getGenericSuperclass();
			} 
		}
		
	}
	@Test
	public void test5() throws NoSuchMethodException, SecurityException {
		Map<String,Object> map = new HashMap<String,Object>();
		Class<?> clazz = map.getClass();
		ParameterizedType paramType = (ParameterizedType) clazz.getGenericSuperclass();
		Type[] types = paramType.getActualTypeArguments();
		Arrays.stream(types).forEach(type -> {System.out.println(type);});
	}
	
	@Test
	public void test6() throws NoSuchMethodException, SecurityException {
		Class<?> clazz = RestView.class;
		Method method = clazz.getMethod("getData");
		Type type = TypeParameterResolver.resolveReturnType(method, clazz);
		System.out.println(type);
		
		
	}
	@Test
	public void test7() throws NoSuchMethodException, SecurityException {
		Class<?> clazz = DefaultRestView.class;
		Method method = clazz.getMethod("getData");
		Type type = TypeParameterResolver.resolveReturnType(method, clazz);
		System.out.println(type);
		
		clazz = DefaultRestView.class;
		method = clazz.getMethod("getRecords");
		type = TypeParameterResolver.resolveReturnType(method, clazz);
		System.out.println(type);
	}
	
	@Test
	public void test8() throws NoSuchMethodException, SecurityException {
		Class<?> clazz = DefaultRestView.class;
		Method method = clazz.getMethod("getData");
		Type type = TypeParameterResolver.resolveReturnType(method, clazz);
		
		System.out.println(type);
		
		
		type = TypeResolver.resolveReturnType(method, clazz);	
		System.out.println(type);
		
		Long now  = System.currentTimeMillis();
		clazz = ScriptDefaultRestView.class;
		method = clazz.getMethod("getData");
		type = TypeParameterResolver.resolveReturnType(method, clazz);
		Long time = System.currentTimeMillis() - now;
		System.out.println(time);
		System.out.println(type);
		
		now = System.currentTimeMillis();
		type = TypeResolver.resolveReturnType(method, clazz);
		time = System.currentTimeMillis() - now;
		System.out.println(time);
		System.out.println(type);

	
		
		
		clazz = DefaultRestView.class;
		method = clazz.getMethod("getRecords");
		now = System.currentTimeMillis();
		type = TypeParameterResolver.resolveReturnType(method, clazz);
		time = System.currentTimeMillis() - now;
		System.out.println(time);
		System.out.println(type);
		

		now = System.currentTimeMillis();
		type = TypeResolver.resolveReturnType(method, clazz);
		time = System.currentTimeMillis() - now;
		System.out.println(time);
		System.out.println(type);
	
	}

	
	class ScriptDefaultRestView extends RestView<List<Method>>{
		
	}
	
	class XmlDefaultRestView extends DefaultRestView{
		
	}
	
	class DefaultRestView extends RestView<String>{
		
	}
	
	
	
	class RestView<T>{
		private Integer code;
		private String message;
		
		private List<T> records;
		private T data;
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public T getData() {
			return data;
		}
		public void setData(T data) {
			this.data = data;
		}
		public List<T> getRecords() {
			return records;
		}
		public void setRecords(List<T> records) {
			this.records = records;
		}
		
	}
	



	
}
