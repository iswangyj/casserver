package org.jasig.cas.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author SxL
 * Created on 9/25/2018 1:45 PM.
 */
@Aspect
public class LogAspect {
    public LogAspect() {
    }

    @Around("(execution (public * org.jasig.cas..*.*(..))) && !(execution( * org.jasig.cas..*.set*(..)))")
    public Object traceMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object returnVal = null;
        Logger logger = this.getLog(proceedingJoinPoint);
        String methodName = proceedingJoinPoint.getSignature().getName();

        Object var10;
        try {
            if (logger.isTraceEnabled()) {
                Object[] args = proceedingJoinPoint.getArgs();
                String arguments;
                if (args != null && args.length != 0) {
                    arguments = Arrays.deepToString(args);
                } else {
                    arguments = "";
                }

                logger.trace("Entering method [{}] with arguments [{}]", methodName, arguments);
            }

            returnVal = proceedingJoinPoint.proceed();
            var10 = returnVal;
        } finally {
            logger.trace("Leaving method [{}] with return value [{}].", methodName, returnVal != null ? returnVal.toString() : "null");
        }

        return var10;
    }

    protected Logger getLog(JoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        return target != null ? LoggerFactory.getLogger(target.getClass()) : LoggerFactory.getLogger(this.getClass());
    }
}
