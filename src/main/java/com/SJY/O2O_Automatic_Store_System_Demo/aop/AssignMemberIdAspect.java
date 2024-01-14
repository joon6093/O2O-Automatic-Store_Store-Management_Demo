package com.SJY.O2O_Automatic_Store_System_Demo.aop;

import com.SJY.O2O_Automatic_Store_System_Demo.config.security.guard.AuthHandler;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

@Aspect
@Component
@Slf4j
public class AssignMemberIdAspect {
    @Before("@annotation(com.SJY.O2O_Automatic_Store_System_Demo.aop.AssignMemberId)")
    public void assignMemberId(JoinPoint joinPoint) {
        // 대상 객체 정보 로깅
        Object target = joinPoint.getTarget();
        String targetClassName = target.getClass().getName();
        log.info("Target class: {}", targetClassName);

        // 메소드 시그니처 정보 로깅
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getName();
        String declaringTypeName = signature.getDeclaringTypeName();
        log.info("Called method: {} in {}", methodName, declaringTypeName);

        // 메소드 파라미터 정보 로깅
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            log.info("Argument value: {}", arg);
        }

        // memberId 할당 로직
        Arrays.stream(args)
                .forEach(arg -> getMethod(arg.getClass(), "setMemberId")
                        .ifPresent(setMemberId -> invokeMethod(arg, setMemberId, AuthHandler.extractMemberId())));
    }

    private Optional<Method> getMethod(Class<?> clazz, String methodName) {
        try {
            return Optional.of(clazz.getMethod(methodName, Long.class));
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    private void invokeMethod(Object obj, Method method, Object... args) {
        try {
            method.invoke(obj, args);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}