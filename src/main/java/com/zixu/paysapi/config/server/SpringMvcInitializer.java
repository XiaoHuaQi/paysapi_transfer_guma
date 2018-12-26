package com.zixu.paysapi.config.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.zixu.paysapi.config.SpringRootConfig;
import com.zixu.paysapi.config.SpringWebConfig;

/**
 * Spring mvc
 * 
 * @author liulibo
 * 1.springmvc配置DispatcherServlet
 * 2.读取SpringConfig.class、springWebConfig.class配置类
 */
@Order(3)
public class SpringMvcInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { SpringRootConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { SpringWebConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);
	}
}