package com.zixu.paysapi.ext.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CustomRepository<E, ID extends Serializable> extends JpaRepository<E, ID> {

	public EntityManager getEntityManager();

	public <T> void persist(T entity);

	public <T> List<T> get(Class<T> entityClass, String... pks);

	public <T> T merge(T entity);

	public <T> void remove(T entity);

	public <T> T get(Class<T> entityClass, String primaryKey);

	public <T> T get(Class<T> entityClass, String primaryKey, Map<String, Object> properties);

	public <T> T get(Class<T> entityClass, String primaryKey, LockModeType lockMode);

	public <T> T get(Class<T> entityClass, String primaryKey, LockModeType lockMode, Map<String, Object> properties);

	public <T> T getReference(Class<T> entityClass, String primaryKey);

	public <T> T findUnique(Class<T> resultClass, String jpql);

	public <T> T findUnique(Class<T> resultClass, String jpql, Map<String, Object> args);

	public <T> T findUnique(Class<T> resultClass, String jpql, Object... args);

	public <T> T nativeFindUnique(Class<T> resultClass, String sql);

	public <T> T nativeFindUnique(Class<T> resultClass, String sql, Map<String, Object> args);

	public <T> T nativeFindUnique(Class<T> resultClass, String sql, Object... args);

	public <T> List<T> find(Class<T> resultClass, String jpql);

	public <T> List<T> find(Class<T> resultClass, String jpql, Object... args);

	public <T> List<T> find(Class<T> resultClass, String jpql, Map<String, Object> args);

	public <T> List<T> nativeFind(Class<T> resultClass, String sql);

	public <T> List<T> nativeFind(Class<T> resultClass, String sql, Object... args);

	public <T> List<T> nativeFind(Class<T> resultClass, String sql, Map<String, Object> args);

	public int executeUpdate(String jpql);

	public int executeUpdate(String jpql, Object... args);

	public int executeUpdate(String jpql, Map<String, Object> args);

	public int nativeExecuteUpdate(String sql);

	public int nativeExecuteUpdate(String sql, Object... args);

	public int nativeExecuteUpdate(String sql, Map<String, Object> args);
	
	public int getCount(String sql,Object... args);
	
}