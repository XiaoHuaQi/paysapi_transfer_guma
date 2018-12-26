package com.zixu.paysapi.config.server;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.springframework.core.Conventions;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.util.IntrospectorCleanupListener;

/**
 * 
 * @author liulibo
 * 1.配置编码过滤器CharacterEncodingFilter
 * 2.配置监听器SingleSignOutHttpSessionListener、IntrospectorCleanupListener
 */
@Order(1)
public class CommonWebInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		registerServletFilter(servletContext, characterEncodingFilter);
		servletContext.addListener(new SingleSignOutHttpSessionListener());
		servletContext.addListener(new IntrospectorCleanupListener());
	}

	protected FilterRegistration.Dynamic registerServletFilter(ServletContext servletContext, Filter filter) {
		String filterName = Conventions.getVariableName(filter);
		Dynamic registration = servletContext.addFilter(filterName, filter);
		if (registration == null) {
			throw new IllegalStateException("Duplicate Filter registration for '" + filterName + "'. Check to ensure the Filter is only configured once.");
		}
		registration.setAsyncSupported(true);
		registration.addMappingForUrlPatterns(getDispatcherTypes(), false, "/*");
		return registration;
	}

	private EnumSet<DispatcherType> getDispatcherTypes() {
		return EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ASYNC);
	}
}
