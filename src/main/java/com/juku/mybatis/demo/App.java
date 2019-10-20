package com.juku.mybatis.demo;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.juku.mybatis.demo.mapper.BlogMapper;
import com.juku.mybatis.demo.mapper.EnterpriseProcessMapper;
import com.juku.mybatis.demo.model.EnterpriseProcess;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	
    	try {
            String config = "mybatis-config.xml";
            InputStream is = Resources.getResourceAsStream(config);
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(is);
            SqlSession session = factory.openSession();
//            BlogMapper mapper =	session.getMapper(BlogMapper.class);
//             mapper.selectBlog(123);
            EnterpriseProcessMapper enterpriseProcessMapper = session.getMapper(EnterpriseProcessMapper.class);
             
            EnterpriseProcess enterpriseProcess = new EnterpriseProcess();
            enterpriseProcess.setId(String.valueOf(System.currentTimeMillis()));
            enterpriseProcess.setType('0');
            int count = enterpriseProcessMapper.insert(enterpriseProcess);
            System.out.println(count);
             session.commit();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	

    }
}
