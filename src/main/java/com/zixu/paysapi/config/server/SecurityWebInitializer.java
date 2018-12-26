package com.zixu.paysapi.config.server;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Spring Security
 * 
 * @author liulibo
 * 1.配置监听器 org.springframework.security.web.session.HttpSessionEventPublisher
 * 2.配置过滤器 springSecurityFilterChain
 */
//@Order(2)
public class SecurityWebInitializer 
//extends AbstractSecurityWebApplicationInitializer 
{

	//@Override
	//protected boolean enableHttpSessionEventPublisher() {
	//	return true;
	//}
}