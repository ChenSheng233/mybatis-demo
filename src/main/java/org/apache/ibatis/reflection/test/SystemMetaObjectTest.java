package org.apache.ibatis.reflection.test;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

public class SystemMetaObjectTest {

	public static void main(String[] args) {
		DataSource dataSource = new UnpooledDataSource();
		MetaObject metaDataSource = SystemMetaObject.forObject(dataSource);
		
		if(metaDataSource.hasGetter("password")) 
			System.out.println("当前对象有password的set方法");
		
		// 设置参数值 setValue(String,Object):void
		metaDataSource.setValue("password", "123456");
		
		// 获取参数值 getValue(String):Object
		Object obj = metaDataSource.getValue("password");
		System.out.println(obj);
		
		// 获取参数值类型 getGetterType(String) Class<?>
		Class<?> clazz = metaDataSource.getGetterType("password");
		if(String.class.isAssignableFrom(clazz))
			System.out.println("password参数类型是String");
		
	}
}
