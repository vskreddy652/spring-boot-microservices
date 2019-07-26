package com.eg.mod.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogService {

	private static Logger logger = LoggerFactory.getLogger(LogService.class);

	@Pointcut("execution(* com.eg.mod..*.*(..)) && !within(com.eg.mod.security.*Filter*)")
	public void logService() {
	}

	/**
	 * "Before" advice in the method beforeMethodStatistics in order to get time in
	 * milliseconds just before the method execution occurs. It calls JointPoint as
	 * a parameter to capture the parameters passed in the method execution.
	 * 
	 * @param jp
	 * @throws Throwable
	 */
	@Before("logService()")
	public void beforeMethod(JoinPoint jp) throws Throwable {
		// Object[] args = jp.getArgs();
		logger.info("Entering....." + jp.getTarget().getClass() + " ;  Method: " + jp.getSignature().toShortString());
		/*
		 * if (null != args && args.length > 0) { for (Object arg : args) {
		 * logger.info(arg.toString()); } }
		 */
	}

	@After("logService()")
	public void afterMethod(JoinPoint jp) {
		// Object[] args = jp.getArgs();
		logger.info("Exiting....." + jp.getTarget().getClass() + " ;  Method: " + jp.getSignature().toShortString());
	}

	// @Around("logService()")
	// public void withinMethod(JoinPoint jp) {
	// }

	@AfterThrowing(pointcut = "logService()", throwing = "ex")
	public void afterThrowing(Exception ex) {

		logger.info("Exception thrown from " + ex.getStackTrace()[0].toString());
		logger.info("Exception is " + ex);

	}

}
