package com.zixu.paysapi.ext.data;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

@NoRepositoryBean
public class CustomRepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable> extends JpaRepositoryFactoryBean<R, T, I> {
	@Override
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new CustomRepositoryFactory(entityManager);
	}

	@NoRepositoryBean
	private static class CustomRepositoryFactory extends JpaRepositoryFactory {

		public CustomRepositoryFactory(EntityManager entityManager) {
			super(entityManager);
		}

		@Override
		protected <T, ID extends Serializable> SimpleJpaRepository<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
			JpaEntityInformation<?, Serializable> entityInformation = getEntityInformation(information.getDomainType());
			if (information.getRepositoryBaseClass() == QueryDslJpaRepository.class) {
				return getTargetRepositoryViaReflection(information, entityInformation, entityManager);
			} else {
				return getTargetRepositoryViaReflection(information, entityInformation, entityManager);
			}
		}

		@Override
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			Class<?> repositoryBaseClass = super.getRepositoryBaseClass(metadata);
			return (repositoryBaseClass == QueryDslJpaRepository.class) ? repositoryBaseClass : CustomJpaRepository.class;
		}
	}
}