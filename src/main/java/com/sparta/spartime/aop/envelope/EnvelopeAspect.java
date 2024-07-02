package com.sparta.spartime.aop.envelope;

import com.sparta.spartime.dto.response.EnvelopeResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class EnvelopeAspect {
    @Around("execution(* com.sparta.spartime.web.controller..*(..))")
    public Object envelopeResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Envelope envelopeAnnotation = method.getAnnotation(Envelope.class);

        String msg = getMessage(envelopeAnnotation, method.getName());

        Object result = joinPoint.proceed();

        if (!(result instanceof ResponseEntity<?> responseEntity)) {
            return result;
        }
        return EnvelopeResponse.wrap(
                responseEntity.getBody(),
                HttpStatus.valueOf(responseEntity.getStatusCode().value()),
                msg
        );
    }

    private String getMessage(Envelope envelopeAnnotation, String methodName) {
        if (envelopeAnnotation == null) {
            return methodName + " ok";
        }
        return envelopeAnnotation.value();
    }
}
