package com.bhnatiuk.uni.bookstore.backend.config.aspect;

import com.bhnatiuk.uni.bookstore.backend.entity.policy.ExceptionWrappingPolicy;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ExceptionMappingAspect {

    private final ExceptionWrappingPolicy policy;

    @Around("@annotation(catchAndMapTo)")
    public Object mapAnyThrowableTo(ProceedingJoinPoint joinPoint,
                                    CatchAndMapTo catchAndMapTo) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw policy.wrap(e, catchAndMapTo.value());
        }
    }
}
