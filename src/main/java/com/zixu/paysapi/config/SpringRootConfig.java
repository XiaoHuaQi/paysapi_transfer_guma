package com.zixu.paysapi.config;




import static com.zixu.paysapi.Constant.BASE_PACKAGES;
import static com.zixu.paysapi.Constant.COMPONENT_SCAN_ASPECTJ;
import static com.zixu.paysapi.Constant.CONFIG_SCAN_ASPECTJ;
import static com.zixu.paysapi.Constant.DAO_SCAN_ASPECTJ;
import static com.zixu.paysapi.Constant.SERVICE_SCAN_ASPECTJ;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = BASE_PACKAGES, useDefaultFilters = false, includeFilters = { @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = { SERVICE_SCAN_ASPECTJ, DAO_SCAN_ASPECTJ, COMPONENT_SCAN_ASPECTJ, CONFIG_SCAN_ASPECTJ }) })
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SpringRootConfig {
	
}