package com.epam.xstack.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class ServiceAspectLogging {
    @Before("execution(* com.epam.xstack.service.*.*(..))")
    public void beforeServiceMethod(JoinPoint joinPoint) {
        log.info("Starting operation: {}", joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "execution(* com.epam.xstack..service.*.*(..))", returning = "result")
    public void afterReturningServiceMethod(JoinPoint joinPoint, Object result) {
        log.info("Completed operation: {}", joinPoint.getSignature().getName());
    }

    @AfterThrowing(pointcut = "execution(* com.epam.xstack..service.*.*(..))", throwing = "error")
    public void afterThrowingServiceMethod(JoinPoint joinPoint, Throwable error) {
        log.error("Operation resulted in error: {}", error.getMessage());
    }

}
