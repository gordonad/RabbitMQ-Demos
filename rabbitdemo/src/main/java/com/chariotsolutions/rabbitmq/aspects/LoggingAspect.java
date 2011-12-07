package com.chariotsolutions.rabbitmq.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

//    @Around(value = "execution(* com.chariotsolutions.rabbitmq.service.MezzageService+.*(..))") //good
    @Around(value = "execution(* com.chariotsolutions.rabbitmq..*.*(..))") //good
    public Object cache(ProceedingJoinPoint point) throws Throwable {
        Object value = null;
        String methodName = point.getSignature().getName();
        logger.debug("**** Invoking Method '{}'", methodName);

        value = point.proceed();
        if (value != null) {
            logger.debug("**** Method '{}' returning Object '{}' containing '{}'", value.getClass().getName(), value.toString());
        }
        return value;
    }
}
