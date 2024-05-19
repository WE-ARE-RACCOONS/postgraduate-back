package com.postgraduate.global.aop.lock;

import org.aspectj.lang.annotation.Pointcut;

public class DistributeLockPointCut {
    @Pointcut("execution(* com.postgraduate.domain.mentoring.application.usecase.MentoringManageUseCase.*(..)) " +
            "&& !execution(* com.postgraduate.domain.mentoring.application.usecase.MentoringManageUseCase.sendFinishMessage(..)) " +
            "&& !execution(* com.postgraduate.domain.mentoring.application.usecase.MentoringManageUseCase.applyMentoring(..))"
    )
    public void allMentoringService() {}
}
