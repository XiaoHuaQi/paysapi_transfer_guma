package com.zixu.paysapi.config.server;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;



/**
 * 拦截器：记录用户操作日志
 * @author zzm
 */
@Aspect
@Component
public class WebLogInterceptor {
/*
	 private static final Log logger = LogFactory.getLog(WebLogInterceptor.class);
	
     @Autowired
	 private BgyLogService bgyLogService;
	 
	
    *//**
     * 定义拦截规则：拦截com.bgy.tour.mvc包下面的所有类中，有@RequestMapping注解的方法。
     *//*
    @Pointcut("execution(public * com.bgy.uproject.mvc.*.*(..))")
    public void controllerMethodPointcut(){}
    

    

    *//**
     * 拦截器具体实现
     * @param pjp
     * @return JsonResult（被拦截方法的执行结果，或需要登录的错误提示。）
     *//*
    @Around("controllerMethodPointcut()") //指定拦截器规则；也可以直接把“execution(* com.xjj.........)”写进这里
    public Object Interceptor(ProceedingJoinPoint pjp){
    	BgyLog log = new BgyLog();
    	log.setCreateTime(new Date());
    	log.setStartTime(new Date());
        long beginTime = System.currentTimeMillis();
        Object ob = null;
//        try {
			try {
				ob = pjp.proceed();
			} catch (Throwable e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			log =  preDo(pjp, log, beginTime);
			if(null!=ob)
				log.setRes(ob.toString());
			else
				log.setRes(null);
	    	try {
		    	//bgyLogService.add(log); 
			} catch (Exception e) {
				logger.error("保存日志失败:"+e.getMessage());
			}
//		   } 
//        catch (Throwable e) {
//				log =  preDo(pjp, log, beginTime);     
//		    	log.setRes(e.getMessage());
//		    	bgyLogService.add(log);
//		    	logger.error("当前controller异常:"+e.getMessage());
//
//		}// ob 为方法的返回值
        return ob;
    }




	private BgyLog preDo(ProceedingJoinPoint pjp, BgyLog log, long beginTime) {
		Date endDate = new Date();
		long usetime= endDate.getTime()-beginTime;
		log.setUseTime(usetime);
		log.setEndTime(endDate);
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes)ra;
		HttpServletRequest request = sra.getRequest();
		log.setUrl(request.getRequestURL().toString());
		log.setReqMethod(request.getMethod());
		log.setClassMethod(signature.getDeclaringTypeName() + "."+ signature.getName());
		log.setIpAddress(request.getRemoteAddr());
		Enumeration enu=request.getParameterNames();  
		StringBuffer sb = new StringBuffer();
		while(enu.hasMoreElements()){  
			String paraName=(String)enu.nextElement(); 
			if(StringUtils.isNotEmpty(paraName))
				sb.append(paraName+":"+request.getParameter(paraName)+",");
		}
		log.setParams(sb.toString());
		return log;
	}*/


}