package com.zixu.paysapi;

public class Constant {
	/**
	 * Global Serialization value for Spring Security classes.
	 * 
	 * N.B. Classes are not intended to be serializable between different
	 * versions. See SEC-1709 for why we still need a serial version.
	 */
	public static final long SERIAL_VERSION_UID = 400L;
	
	/**
	 * 代码扫描根包
	 */
	public static final String BASE_PACKAGES = "com.zixu";

	/**
	 * Spring web 扫描 aspectj
	 */
	public static final String CONTROLLER_SCAN_ASPECTJ = "@org.springframework.stereotype.Controller com.zixu..*Controller";
	public static final String CONTROLLER_REST_SCAN_ASPECTJ = "@org.springframework.web.bind.annotation.RestController com.zixu..*Controller";
	public static final String CONTROLLER_ADVICE_SCAN_ASPECTJ = "@org.springframework.web.bind.annotation.ControllerAdvice com.zixu..*ControllerAdvice";

	/**
	 * Spring root 扫描 aspectj
	 */
	public static final String SERVICE_SCAN_ASPECTJ = "@org.springframework.stereotype.Service (com.zixu..service.impl.*ServiceImpl|| com.zixu..formula.*Service)";
	public static final String DAO_SCAN_ASPECTJ = "@org.springframework.stereotype.Repository com.zixu..dao.impl.*DaoImpl";
	public static final String COMPONENT_SCAN_ASPECTJ = "@org.springframework.stereotype.Component (com.zixu..*ApplicationListener || com.zixu..*Component || com.zixu..*Endpoint || com.zixu..parser.*Parser)";
	public static final String CONFIG_SCAN_ASPECTJ = "@org.springframework.context.annotation.Configuration com.zixu..config.ext.*Config";
	/**
	 * JPA Data 扫描 aspectj
	 */
	public static final String JPA_REPOSITORY_SCAN_ASPECTJ = "com.zixu..jpa.dao.*Dao && org.springframework.data.repository.Repository+";

	/**
	 * Solr Data 扫描 aspectj
	 */
	public static final String SOLR_REPOSITORY_SCAN_ASPECTJ = "com.zixu..solr.dao.*Dao && org.springframework.data.repository.Repository+";

	/**
	 * 实体扫描根包
	 */
	public static final String[] ENTITY_PACKAGES_SCAN = { BASE_PACKAGES + ".**.entity" };

	/**
	 * 自定义ID生成器
	 */
	public static final String ID_GENERATED_NAME = "CodeGenerator";
	public static final String ID_GENERATED_STRATEGY = "com.zixu.paysapi.ext.jpa.code.CodeGenerator";
	/**
	 * 系统配置文件路径
	 */
	public static final String CONFIG_PROPERTY_SOURCE = "classpath:config.properties";
	/**
	 * jdbc配置文件路径
	 */
	public static final String JDBC_PROPERTY_SOURCE = "classpath:jdbc.properties";
	/**
	 * redis配置文件路径
	 */
	public static final String REDIS_PROPERTY_SOURCE = "classpath:redis.properties";
	/**
	 * activiti配置文件路径
	 */
	public static final String ENGINE_PROPERTY_SOURCE = "classpath:engine.properties";
	/**
	 * solr配置文件路径
	 */
	public static final String SOLR_PROPERTY_SOURCE = "classpath:solr.properties";
	/**
	 * email配置文件路径
	 */
	public static final String EMAIL_PROPERTY_SOURCE = "classpath:email.properties";

}