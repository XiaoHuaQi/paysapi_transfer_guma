package com.zixu.paysapi.config;

import static com.zixu.paysapi.Constant.BASE_PACKAGES;
import static com.zixu.paysapi.Constant.CONTROLLER_ADVICE_SCAN_ASPECTJ;
import static com.zixu.paysapi.Constant.CONTROLLER_REST_SCAN_ASPECTJ;
import static com.zixu.paysapi.Constant.CONTROLLER_SCAN_ASPECTJ;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.zixu.paysapi.config.server.WebLogInterceptor;


/**
 * MVC配置
 * 
 * @author liulibo
 * 
 */
@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = BASE_PACKAGES, useDefaultFilters = false, includeFilters = { @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = { CONTROLLER_SCAN_ASPECTJ, CONTROLLER_REST_SCAN_ASPECTJ, CONTROLLER_ADVICE_SCAN_ASPECTJ }) })
public class SpringWebConfig extends WebMvcConfigurerAdapter {

	/**
	 * 是否启用容器默认Servlet处理器
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	/*
	@Override
    public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("*")
			.allowedMethods("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE");
    }
	*/
	
	/*
	 * PC端的视图解析器
	 */
	@Bean
	public ViewResolver jspViewResolver() {
		InternalResourceViewResolver jspViewResolver = new InternalResourceViewResolver();
		jspViewResolver.setCache(false);//解析视图名称是否启用缓存(默认启用)
		jspViewResolver.setSuffix(".jsp");
		//jspViewResolver.setPrefix("/WEB-INF/pages/");
		jspViewResolver.setRequestContextAttribute("requestContext");
		jspViewResolver.setContentType("text/html;charset=UTF-8");
		return jspViewResolver;
	}

	/**
	 * 手机端的视图解析器
	 * @return
	@Bean
	public LiteDeviceDelegatingViewResolver liteDeviceAwareViewResolver() {
		InternalResourceViewResolver delegate = new InternalResourceViewResolver();
		delegate.setCache(false);//解析视图名称是否启用缓存(默认启用)
		delegate.setSuffix(".jsp");
		delegate.setPrefix("/WEB-INF/views/");
		delegate.setRequestContextAttribute("requestContext");
		delegate.setContentType("text/html;charset=UTF-8");
		LiteDeviceDelegatingViewResolver resolver = new LiteDeviceDelegatingViewResolver(delegate);
		resolver.setEnableFallback(true);// 特定前缀获取不到时,采用无前缀获取
		resolver.setMobilePrefix("mobile/");
		resolver.setTabletPrefix("tablet/");
		resolver.setNormalPrefix("normal/");
		return resolver;
	}
	 */
	
	/**
	 * 集成文件上传
	 * @return
	 */
	@Bean
    public MultipartResolver multipartResolver(){  
        CommonsMultipartResolver bean=new CommonsMultipartResolver();  
        bean.setDefaultEncoding("UTF-8");  
        //bean.setMaxUploadSize(25165824);  
        return bean;  
    }
	
//	 @Bean
//	 LogInitializer logInitializer() {
//	        return new LogInitializer();
//	    }
//	
//	 public void addInterceptors(InterceptorRegistry registry) {
//	        registry.addInterceptor(logInitializer()).addPathPatterns("/**");  //对来自/user/** 这个链接来的请求进行拦截
//	    }
	 
//	 @Bean
//	 WebLogInterceptor webLogInterceptor() {
//	        return new WebLogInterceptor();
//	    }
}
