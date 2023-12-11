package com.epam.xstack.aspect;

import com.epam.xstack.service.impl.AppMetricsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.UUID;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ControllerAspectLogging {

    private final AppMetricsService appMetricsService;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping) || " + "@annotation(org.springframework.web.bind.annotation.PostMapping) || " + "@annotation(org.springframework.web.bind.annotation.PutMapping) || " + "@annotation(org.springframework.web.bind.annotation.DeleteMapping) || " + "@annotation(org.springframework.web.bind.annotation.PatchMapping) || " + "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void webMappingMethods() {
    }


    private void logPaths(String methodType, StringBuilder path) {
        log.info("Method Type: {}, Path: {}", methodType, path.toString());

    }

    @Before("webMappingMethods()")
    public void beforeRequest(JoinPoint joinPoint) {
        appMetricsService.addRequestCount();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        StringBuilder url = new StringBuilder();
        if (method.getDeclaringClass().isAnnotationPresent(RequestMapping.class)) {
            url.append(String.join("", method.getDeclaringClass().getAnnotation(RequestMapping.class).value()));
        }
        String transactionId = UUID.randomUUID().toString();
        MDC.put("transactionId", transactionId);
        log.info("Transaction started with ID: {}", transactionId);
        if (method.getAnnotations().length == 0) return;

        if (method.isAnnotationPresent(GetMapping.class)) {
            GetMapping getMapping = method.getAnnotation(GetMapping.class);
            logPaths("GET", url.append(String.join("", getMapping.value())));
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            PostMapping postMapping = method.getAnnotation(PostMapping.class);
            logPaths("POST", url.append(String.join("", postMapping.value())));
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            PutMapping putMapping = method.getAnnotation(PutMapping.class);
            logPaths("PUT", url.append(String.join("", putMapping.value())));
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
            logPaths("DELETE", url.append(String.join("", deleteMapping.value())));
        } else if (method.isAnnotationPresent(PatchMapping.class)) {
            PatchMapping patchMapping = method.getAnnotation(PatchMapping.class);
            logPaths("PATCH", url.append(String.join("", patchMapping.value())));
        }

    }

    @AfterReturning(pointcut = "webMappingMethods()", returning = "response")
    public void afterReturning(Object response) {
        if (response instanceof ResponseEntity<?> res) {
            log.info("Transaction completed with response status: {}", res.getStatusCode());
            log.info("Transaction completed with response message: {}", ((ResponseEntity<?>) response).getBody());
        }
        MDC.clear();
    }

    @AfterThrowing(pointcut = "webMappingMethods()", throwing = "error")
    public void afterThrowing(Throwable error) {
        log.error("Transaction resulted in error: {}", error.getMessage());
        MDC.clear();
    }
}
