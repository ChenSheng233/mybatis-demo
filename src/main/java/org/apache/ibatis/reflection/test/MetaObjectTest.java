package org.apache.ibatis.reflection.test;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;

public class MetaObjectTest {
	  
	  // 对象创建工厂，通过底层使用反射 API使用指定的构造器创建对象
	  public static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	  
	  public static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
	public static void main(String[] args) {
		DataSource dataSource = new UnpooledDataSource();
		// 
		MetaObject metaObjectDataSource = MetaObject.forObject(dataSource , DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
	
		
	}
}
