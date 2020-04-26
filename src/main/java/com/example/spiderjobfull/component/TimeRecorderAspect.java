package com.example.spiderjobfull.component;

import com.egoist.parent.common.annotation.EgoistTimeRecorder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 计时切面
 */
@Component
@Aspect
public class TimeRecorderAspect {

    Logger logger = LoggerFactory.getLogger(TimeRecorderAspect.class);

    @Around("@annotation(timeRecorder)")
    public void arround(ProceedingJoinPoint pjp, EgoistTimeRecorder timeRecorder) {
        try {
            long startTime = System.currentTimeMillis();
            pjp.proceed();
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            logger.error(String.format("%s执行方法耗时 :%s毫秒", timeRecorder.desc(), duration));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
