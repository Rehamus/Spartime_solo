package com.sparta.spartime.aop.tracelog;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@Aspect
public class TraceLogAspect {
    private final TraceLog traceLog;

    public TraceLogAspect(TraceLog traceLog) {
        this.traceLog = traceLog;
    }

    @Around("execution(* com.sparta.spartime.web.controller..*(..)) || execution(* com.sparta.spartime.service..*(..))")
    public Object trace(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus status = null;
        try {
            String message = joinPoint.getSignature()
                    .toShortString()
                    .replace("..", formatArguments(joinPoint.getArgs()));
            status = traceLog.begin(message);

            Object result = joinPoint.proceed();

            traceLog.end(status, result == null ? "" : result.toString());
            return result;
        } catch (Exception e) {
            traceLog.exception(status, e);
            throw e;
        }
    }

    private String formatArguments(Object[] args) {
        return Arrays.stream(args)
                .map(arg -> {
                    if (arg == null) {
                        return "null";
                    }
                    return arg.toString();
                })
                .collect(Collectors.joining(", "));
    }
}
