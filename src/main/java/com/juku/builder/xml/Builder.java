package com.juku.builder.xml;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.ResultMapResolver;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.builder.xml.XMLStatementBuilder;
import org.apache.ibatis.datasource.DataSourceFactory;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Discriminator;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.xmltags.XMLScriptBuilder;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.TypeAliasRegistry;

import com.juku.mybatis.demo.mapper.BlogMapper;
import com.juku.mybatis.demo.model.Blog;

public class Builder {

	public static void main(String[] args)
			throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, SecurityException, NoSuchMethodException, ClassNotFoundException {
//		_XMLConfigBuilder();
//		_XMLMapperBuilder();
//		_ResultMapResolver();
//		_MapperBuilderAssistant();
//		_LanguageDriver();
		_XMLScriptBuilder();
	}

	public static void _XMLConfigBuilder() throws IOException {
		String config = "./mybatis-config.xml";

		XMLConfigBuilder parser = new XMLConfigBuilder(Resources.getResourceAsStream(config), null, null);
		Configuration configuration = parser.parse();

		DefaultSqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
		Blog blog = blogMapper.selectBlog(1);
		System.out.println(ReflectionToStringBuilder.toString(blog, ToStringStyle.MULTI_LINE_STYLE));
	}

	public static void _XMLMapperBuilder() throws IOException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, SecurityException, NoSuchMethodException {
		Configuration configuration = new Configuration();
		prepareConfiguration(configuration);

		// XMLMapperBuilder 负责 mapper映射文件
		String url = null;
		String mapper = "./mapper/BlogMapper.xml";
		XMLMapperBuilder mapperParser = new XMLMapperBuilder(Resources.getResourceAsStream(mapper), configuration, url,
				null);
		mapperParser.parse();

		DefaultSqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
		Blog blog = blogMapper.selectBlog(1);
		System.out.println(ReflectionToStringBuilder.toString(blog, ToStringStyle.MULTI_LINE_STYLE));
	}

	public static void _ResultMapResolver()
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, IOException, ClassNotFoundException {
		Configuration configuration = new Configuration();
		prepareConfiguration(configuration);

		List<ResultMapping> resultMappings = new ArrayList<>();
		pareparedResultMapping(resultMappings, configuration);

		String resource = "./mapper/BlogMapper.xml";
		String currentNamespace = "com.juku.mybatis.demo.mapper.BlogMapper";
		Class<?> typeClass = Blog.class;
		String id = "BlogResultMap";
		String extend = null;
		Boolean autoMapping = true;
		Discriminator discriminator = null;

		MapperBuilderAssistant builderAssistant = new MapperBuilderAssistant(configuration, resource);
		// 设置namespace，与mapper接口保持一致。一定要设置
		builderAssistant.setCurrentNamespace(currentNamespace);

		ResultMapResolver resultMapResolver = new ResultMapResolver(builderAssistant, id, typeClass, extend,
				discriminator, resultMappings, autoMapping);
		// 内部使用MapperBuilderAssistant将ResultMap添加进configuration
		ResultMap resultMap = resultMapResolver.resolve();

		// 添加SQL语句
		XPathParser parser = new XPathParser(Resources.getResourceAsStream(resource), true,
				configuration.getVariables(), new XMLMapperEntityResolver());
		XNode context = parser.evalNode("/mapper");
		List<XNode> list = context.evalNodes("select|insert|update|delete");
		for (XNode contenxt : list) {
			XMLStatementBuilder statementParser = new XMLStatementBuilder(configuration, builderAssistant, contenxt,
					null);
			statementParser.parseStatementNode();
		}

		// 添加mapper接口
		String namespace = builderAssistant.getCurrentNamespace();
		pareparedMapper(namespace, configuration);

		DefaultSqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
		Blog blog = blogMapper.selectBlog(1);
		System.out.println(ReflectionToStringBuilder.toString(blog, ToStringStyle.MULTI_LINE_STYLE));

	}

	public static void _MapperBuilderAssistant()
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, IOException, ClassNotFoundException {
		Configuration configuration = new Configuration();
		prepareConfiguration(configuration);

		List<ResultMapping> resultMappings = new ArrayList<>();
		pareparedResultMapping(resultMappings, configuration);

		String resource = "./mapper/BlogMapper.xml";
		String currentNamespace = "com.juku.mybatis.demo.mapper.BlogMapper";
		Class<?> typeClass = Blog.class;
		String id = "BlogResultMap";
		String extend = null;
		Boolean autoMapping = true;
		Discriminator discriminator = null;

		MapperBuilderAssistant builderAssistant = new MapperBuilderAssistant(configuration, resource);
		builderAssistant.setCurrentNamespace(currentNamespace);

		// 内部将ResultMap添加进configuration
		builderAssistant.addResultMap(id, typeClass, extend, discriminator, resultMappings, autoMapping);

		// 添加SQL语句
		XPathParser parser = new XPathParser(Resources.getResourceAsStream(resource), true,
				configuration.getVariables(), new XMLMapperEntityResolver());
		XNode context = parser.evalNode("/mapper");
		List<XNode> list = context.evalNodes("select|insert|update|delete");
		for (XNode contenxt : list) {
			XMLStatementBuilder statementParser = new XMLStatementBuilder(configuration, builderAssistant, contenxt,
					null);
			statementParser.parseStatementNode();
		}

		// 添加mapper接口
		String namespace = builderAssistant.getCurrentNamespace();
		pareparedMapper(namespace, configuration);

		DefaultSqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
		Blog blog = blogMapper.selectBlog(1);
		System.out.println(ReflectionToStringBuilder.toString(blog, ToStringStyle.MULTI_LINE_STYLE));

	}

	public static void _LanguageDriver() throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException, ClassNotFoundException {
		String resource = "./mapper/BlogMapper.xml";
		String currentNamespace = "com.juku.mybatis.demo.mapper.BlogMapper";
		Class<?> typeClass = Blog.class;
		String id = "BlogResultMap";
		String extend = null;
		Boolean autoMapping = true;
		Discriminator discriminator = null;
		Configuration configuration = new Configuration();
		prepareConfiguration(configuration);
		

		

		
		MapperBuilderAssistant builderAssistant = new MapperBuilderAssistant(configuration, resource);
		builderAssistant.setCurrentNamespace(currentNamespace);

		// 添加mapper接口
		String namespace = builderAssistant.getCurrentNamespace();
		pareparedMapper(namespace, configuration);
		
		List<ResultMapping> resultMappings = new ArrayList<>();
		pareparedResultMapping(resultMappings, configuration);
		
		ResultMapResolver resultMapResolver = new ResultMapResolver(builderAssistant, id, typeClass, extend,
				discriminator, resultMappings, autoMapping);
		// 内部使用MapperBuilderAssistant将ResultMap添加进configuration
		resultMapResolver.resolve();
		
		Class<?> parameterTypeClass = null;



		XPathParser parser = new XPathParser(Resources.getResourceAsStream(resource), true,
				configuration.getVariables(), new XMLMapperEntityResolver());
		XNode context = parser.evalNode("/mapper");
		List<XNode> list = context.evalNodes("select|insert|update|delete");
		for (XNode script : list) {
			String scriptId = script.getStringAttribute("id");
			String nodeName = script.getNode().getNodeName();
			SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));
			KeyGenerator keyGenerator;
			String keyStatementId = id + SelectKeyGenerator.SELECT_KEY_SUFFIX;
			keyStatementId = builderAssistant.applyCurrentNamespace(keyStatementId, true);
			if (configuration.hasKeyGenerator(keyStatementId)) {
				keyGenerator = configuration.getKeyGenerator(keyStatementId);
			} else {
				keyGenerator = script.getBooleanAttribute("useGeneratedKeys",
						configuration.isUseGeneratedKeys() && SqlCommandType.INSERT.equals(sqlCommandType))
								? Jdbc3KeyGenerator.INSTANCE
								: NoKeyGenerator.INSTANCE;
			}
			String lang = script.getStringAttribute("lang");
			LanguageDriver languageDriver = configuration
					.getLanguageDriver(configuration.getTypeAliasRegistry().resolveAlias(lang));
			String parameterMap = script.getStringAttribute("parameterMap");
			SqlSource sqlSource = languageDriver.createSqlSource(configuration, script, parameterTypeClass);
			StatementType statementType = StatementType
					.valueOf(script.getStringAttribute("statementType", StatementType.PREPARED.toString()));
			Integer fetchSize = script.getIntAttribute("fetchSize");
			Integer timeout = script.getIntAttribute("timeout");
			String resultType = script.getStringAttribute("resultType");
			Class<?> resultTypeClass = configuration.getTypeAliasRegistry().resolveAlias(resultType);
			Class<?> parameterType = configuration.getTypeAliasRegistry()
					.resolveAlias(script.getStringAttribute("parameterType"));
			String resultMap = script.getStringAttribute("resultMap");
			boolean isSelect = sqlCommandType == SqlCommandType.SELECT;
			boolean flushCache = script.getBooleanAttribute("flushCache", !isSelect);
			boolean useCache = script.getBooleanAttribute("useCache", isSelect);
			boolean resultOrdered = script.getBooleanAttribute("resultOrdered", false);
			ResultSetType resultSetTypeEnum = resolveResultSetType(script.getStringAttribute("resultSetType"));
			if (resultSetTypeEnum == null) {
				resultSetTypeEnum = configuration.getDefaultResultSetType();
			}
			String keyProperty = script.getStringAttribute("keyProperty");
			String databaseId = script.getStringAttribute("databaseId");
			String resultSets = script.getStringAttribute("resultSets");
			String keyColumn = script.getStringAttribute("keyColumn");
			builderAssistant.addMappedStatement(scriptId, sqlSource, statementType, sqlCommandType, fetchSize, timeout,
					parameterMap, parameterType, resultMap, resultTypeClass, resultSetTypeEnum, flushCache, useCache,
					resultOrdered, keyGenerator, keyProperty, keyColumn, databaseId, languageDriver, resultSets);
		
		
			
		}
		
		DefaultSqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
		Blog blog = blogMapper.selectBlog(1);
		System.out.println(ReflectionToStringBuilder.toString(blog, ToStringStyle.MULTI_LINE_STYLE));

	}

	public static void _XMLScriptBuilder() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, IOException {
		String id = "BlogResultMap";
		Class<?> typeClass = Blog.class;
		String resource = "./mapper/BlogMapper.xml";
		String currentNamespace = "com.juku.mybatis.demo.mapper.BlogMapper";
		String extend = null;
		Boolean autoMapping = true;
		Discriminator discriminator = null;
		Configuration configuration = new Configuration();
		prepareConfiguration(configuration);
		
		MapperBuilderAssistant builderAssistant = new MapperBuilderAssistant(configuration, resource);
		builderAssistant.setCurrentNamespace(currentNamespace);
		// 添加mapper接口
		String namespace = builderAssistant.getCurrentNamespace();
		pareparedMapper(namespace, configuration);
		
		
		List<ResultMapping> resultMappings = new ArrayList<>();
		pareparedResultMapping(resultMappings, configuration);
		
		ResultMapResolver resultMapResolver = new ResultMapResolver(builderAssistant, id, typeClass, extend,
				discriminator, resultMappings, autoMapping);
		// 内部使用MapperBuilderAssistant将ResultMap添加进configuration
		resultMapResolver.resolve();
		
		
		Class<?> parameterTypeClass = null;
		
		XPathParser parser = new XPathParser(Resources.getResourceAsStream(resource), true,
				configuration.getVariables(), new XMLMapperEntityResolver());
		XNode context = parser.evalNode("/mapper");
		List<XNode> list = context.evalNodes("select|insert|update|delete");
		for(XNode script:list) {
			String scriptId = script.getStringAttribute("id");
			String nodeName = script.getNode().getNodeName();
			SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));
			KeyGenerator keyGenerator;
			String keyStatementId = id + SelectKeyGenerator.SELECT_KEY_SUFFIX;
			keyStatementId = builderAssistant.applyCurrentNamespace(keyStatementId, true);
			if (configuration.hasKeyGenerator(keyStatementId)) {
				keyGenerator = configuration.getKeyGenerator(keyStatementId);
			} else {
				keyGenerator = script.getBooleanAttribute("useGeneratedKeys",
						configuration.isUseGeneratedKeys() && SqlCommandType.INSERT.equals(sqlCommandType))
								? Jdbc3KeyGenerator.INSTANCE
								: NoKeyGenerator.INSTANCE;
			}
			String lang = script.getStringAttribute("lang");
			LanguageDriver languageDriver = configuration
					.getLanguageDriver(configuration.getTypeAliasRegistry().resolveAlias(lang));
			String parameterMap = script.getStringAttribute("parameterMap");
			XMLScriptBuilder builder = new XMLScriptBuilder(configuration, script, parameterTypeClass);
			SqlSource sqlSource = builder.parseScriptNode();
			//SqlSource sqlSource = languageDriver.createSqlSource(configuration, script, parameterTypeClass);
			StatementType statementType = StatementType
					.valueOf(script.getStringAttribute("statementType", StatementType.PREPARED.toString()));
			Integer fetchSize = script.getIntAttribute("fetchSize");
			Integer timeout = script.getIntAttribute("timeout");
			String resultType = script.getStringAttribute("resultType");
			Class<?> resultTypeClass = configuration.getTypeAliasRegistry().resolveAlias(resultType);
			Class<?> parameterType = configuration.getTypeAliasRegistry()
					.resolveAlias(script.getStringAttribute("parameterType"));
			String resultMap = script.getStringAttribute("resultMap");
			boolean isSelect = sqlCommandType == SqlCommandType.SELECT;
			boolean flushCache = script.getBooleanAttribute("flushCache", !isSelect);
			boolean useCache = script.getBooleanAttribute("useCache", isSelect);
			boolean resultOrdered = script.getBooleanAttribute("resultOrdered", false);
			ResultSetType resultSetTypeEnum = resolveResultSetType(script.getStringAttribute("resultSetType"));
			if (resultSetTypeEnum == null) {
				resultSetTypeEnum = configuration.getDefaultResultSetType();
			}
			String keyProperty = script.getStringAttribute("keyProperty");
			String databaseId = script.getStringAttribute("databaseId");
			String resultSets = script.getStringAttribute("resultSets");
			String keyColumn = script.getStringAttribute("keyColumn");
			builderAssistant.addMappedStatement(scriptId, sqlSource, statementType, sqlCommandType, fetchSize, timeout,
					parameterMap, parameterType, resultMap, resultTypeClass, resultSetTypeEnum, flushCache, useCache,
					resultOrdered, keyGenerator, keyProperty, keyColumn, databaseId, languageDriver, resultSets);
		
		}
		
		DefaultSqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
		Blog blog = blogMapper.selectBlog(1);
		System.out.println(ReflectionToStringBuilder.toString(blog, ToStringStyle.MULTI_LINE_STYLE));

	}
	
	public static void _SqlSource() {
		
	}
	
	protected static ResultSetType resolveResultSetType(String alias) {
		if (alias == null) {
			return null;
		}
		try {
			return ResultSetType.valueOf(alias);
		} catch (IllegalArgumentException e) {
			throw new BuilderException("Error resolving ResultSetType. Cause: " + e, e);
		}
	}

	public static void pareparedResultMapping(List<ResultMapping> resultMappings, Configuration configuration) {
		List<ResultFlag> flags = new ArrayList<>();
		flags.add(ResultFlag.ID);
		String property = "id";
		String column = "ID";
		Class<?> propertyTypeClass = String.class;
		ResultMapping resultMapping = new ResultMapping.Builder(configuration, property, column, propertyTypeClass)
				.build();
		resultMappings.add(resultMapping);

		property = "title";
		column = "TITLE";
		propertyTypeClass = String.class;
		resultMapping = new ResultMapping.Builder(configuration, property, column, propertyTypeClass).build();
		resultMappings.add(resultMapping);

		property = "content";
		column = "CONTENT";
		propertyTypeClass = String.class;
		resultMapping = new ResultMapping.Builder(configuration, property, column, propertyTypeClass).build();
		resultMappings.add(resultMapping);
	}

	public static void pareparedMapper(String namespace, Configuration configuration) throws ClassNotFoundException {
		Class<?> mapperTypeClass = Resources.classForName(namespace);
		configuration.addLoadedResource("namespace:" + namespace);
		configuration.addMapper(mapperTypeClass);
	}

	public static void prepareConfiguration(Configuration configuration)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		TypeAliasRegistry typeAliasRegistry = configuration.getTypeAliasRegistry();
		typeAliasRegistry.registerAlias(Blog.class);

		String transactionManagerType = "JDBC";
		TransactionFactory txFactory = (TransactionFactory) typeAliasRegistry.resolveAlias(transactionManagerType)
				.getDeclaredConstructor().newInstance();

		Properties props = new Properties();
		props.put("driver", "com.mysql.cj.jdbc.Driver");
		props.put("url",
				"jdbc:mysql://localhost:3306/test?useUnicode=true&amp;characterEncoding=utf8&amp;autoReconnect=true&amp;failOverReadOnly=false");
		props.put("username", "root");
		props.put("password", "123456");
		String dataSourceType = "POOLED";
		DataSourceFactory dsFactory = (DataSourceFactory) typeAliasRegistry.resolveAlias(dataSourceType)
				.getDeclaredConstructor().newInstance();
		dsFactory.setProperties(props);
		DataSource dataSource = dsFactory.getDataSource();

		String environmentId = "development";
		Environment.Builder environmentBuilder = new Environment.Builder(environmentId).transactionFactory(txFactory)
				.dataSource(dataSource);
		configuration.setEnvironment(environmentBuilder.build());
	}
}
