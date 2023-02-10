package com.zxh.aspect;

import com.alibaba.fastjson.JSON;
import com.zxh.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@Slf4j
public class LogAspect {
    @Pointcut("@annotation(com.zxh.annotation.SystemLog)")
    public void pc(){

    }
    @Around("pc()")
    public Object printLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object proceed = null;
        try{
            handlerBefore(proceedingJoinPoint);
            proceed = proceedingJoinPoint.proceed();
            handlerAfter(proceed);
        }finally {
            // 结束后换行
            log.info("=======End=======" + System.lineSeparator());
        }
        return proceed;
    }

    private void handlerAfter(Object proceed) {
        // 打印出参
        log.info("Response       : {}",JSON.toJSON(proceed));
    }

    private void handlerBefore(ProceedingJoinPoint proceedingJoinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        log.info("=======Start=======");
        // 打印请求 URL
        log.info("URL            : {}",request.getRequestURL());
        // 打印描述信息

        //获得被增强方法的注解对象
        String s = getBusinessName(proceedingJoinPoint);

        log.info("BusinessName   : {}",s);
        // 打印 Http method
        log.info("HTTP Method    : {}",request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}",proceedingJoinPoint.getSignature().getDeclaringTypeName(),((MethodSignature) proceedingJoinPoint.getSignature()).getName());
        // 打印请求的 IP
        log.info("IP             : {}",request.getRemoteHost());
        // 打印请求入参
        log.info("Request Args   : {}", JSON.toJSONString(proceedingJoinPoint.getArgs()));
    }

    private String getBusinessName(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        SystemLog annotation = signature.getMethod().getAnnotation(SystemLog.class);
        String s = annotation.businessName();
        return s;
    }


}
