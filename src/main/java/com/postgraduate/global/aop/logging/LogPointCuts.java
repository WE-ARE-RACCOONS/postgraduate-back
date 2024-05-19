package com.postgraduate.global.aop.logging;

import org.aspectj.lang.annotation.Pointcut;

//AOP사용
public class LogPointCuts {

    /**
     * 우선은 모든 컨트롤러, 모든 서비스에서 동작하도록 함 이 또한 수정 가능
     */
    @Pointcut("execution(* com.postgraduate.domain..presentation..*(..))")
    public void allController() {}
    @Pointcut("execution(* com.postgraduate.domain..application.usecase..*(..))")
    public void allService() {}
}
