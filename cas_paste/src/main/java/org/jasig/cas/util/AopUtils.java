package org.jasig.cas.util;

import org.aspectj.lang.JoinPoint;

/**
 * @author SxL
 * Created on 9/25/2018 5:08 PM.
 */
public final class AopUtils {
    private AopUtils() {
    }

    public static JoinPoint unWrapJoinPoint(JoinPoint point) {
        JoinPoint naked;
        for(naked = point; naked.getArgs().length > 0 && naked.getArgs()[0] instanceof JoinPoint; naked = (JoinPoint)naked.getArgs()[0]) {
            ;
        }

        return naked;
    }
}
