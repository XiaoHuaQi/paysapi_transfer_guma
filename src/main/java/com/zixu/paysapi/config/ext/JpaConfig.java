package com.zixu.paysapi.config.ext;

import static com.zixu.paysapi.Constant.ENTITY_PACKAGES_SCAN;
import static com.zixu.paysapi.Constant.JDBC_PROPERTY_SOURCE;
import static com.zixu.paysapi.Constant.JPA_REPOSITORY_SCAN_ASPECTJ;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.jolbox.bonecp.BoneCPDataSource;
import com.zixu.paysapi.ext.BaseDao;
import com.zixu.paysapi.ext.data.CustomRepositoryFactoryBean;

@Configuration //类比spring的配置文件
@PropertySource(JDBC_PROPERTY_SOURCE) //读取配置文件,通过env.getProperty()方法获取值
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableJpaRepositories(basePackages = "com.zixu.paysapi.jpa.dao", includeFilters = { @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = { JPA_REPOSITORY_SCAN_ASPECTJ }) }, repositoryFactoryBeanClass = CustomRepositoryFactoryBean.class, repositoryImplementationPostfix = "Impl")
// @EnableJpaAuditing
public class JpaConfig {
	@Autowired
	protected Environment env;

	/**
	 * 数据源
	 * @return
	 */
	@Bean
	public DataSource dataSource() {
		BoneCPDataSource dataSource = new BoneCPDataSource();
		dataSource.setDriverClass(env.getProperty("default.driverClassName"));
		dataSource.setJdbcUrl(env.getProperty("default.url"));
		dataSource.setUsername(env.getProperty("default.username"));
		dataSource.setPassword(env.getProperty("default.password"));
		dataSource.setPartitionCount(env.getProperty("default.partitionCount", Integer.class));
		dataSource.setMaxConnectionsPerPartition(env.getProperty("default.maxConnectionsPerPartition", Integer.class));
		dataSource.setMinConnectionsPerPartition(env.getProperty("default.minConnectionsPerPartition", Integer.class));
		dataSource.setIdleConnectionTestPeriodInMinutes((long) env.getProperty("default.idleConnectionTestPeriodInMinutes", Integer.class));
		dataSource.setIdleMaxAgeInMinutes((long) env.getProperty("default.idleMaxAgeInMinutes", Integer.class));
		dataSource.setAcquireIncrement(env.getProperty("default.acquireIncrement", Integer.class));
		dataSource.setDisableConnectionTracking(true);
		return dataSource;
	}

	@Bean
	public BaseDao baseDao(EntityManager entityManager) {
		BaseDao baseDao = new BaseDao();
		baseDao.setEntityManager(entityManager);
		return baseDao;
	}

	/**
	 * 事务管理器
	 * 
	 * @param emf
	 * @return
	 */
	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}

	/**
	 * 供应商适配器
	 * 
	 * @return
	 */
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		jpaVendorAdapter.setDatabasePlatform(env.getProperty("default.dialect"));
		jpaVendorAdapter.setGenerateDdl(false);
		jpaVendorAdapter.setShowSql(false);
		return jpaVendorAdapter;
	}

	/**
	 * 实体管理器工厂
	 * 
	 * @return
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean lemfb = new LocalContainerEntityManagerFactoryBean();
		lemfb.setDataSource(dataSource());
		lemfb.setPersistenceUnitName("default_unit");
		lemfb.setJpaVendorAdapter(jpaVendorAdapter());
		lemfb.setPackagesToScan(ENTITY_PACKAGES_SCAN);
		lemfb.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
		return lemfb;
	}
	
	/**
	 * 序列号
	 * 
	 * @return
	 */
	@Bean
	public OracleSequenceMaxValueIncrementer oracleIncr() {
		OracleSequenceMaxValueIncrementer oracleIncr = 
		 new OracleSequenceMaxValueIncrementer(dataSource(), "SEQ_IT_NUM");
		oracleIncr.setPaddingLength(6);
		return oracleIncr;
	}
}
