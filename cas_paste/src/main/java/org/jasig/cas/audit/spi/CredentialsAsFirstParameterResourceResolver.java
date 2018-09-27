package org.jasig.cas.audit.spi;

import com.github.inspektr.audit.spi.AuditResourceResolver;
import org.aspectj.lang.JoinPoint;
import org.jasig.cas.util.AopUtils;

import java.util.Arrays;

/**
 * @author SxL
 * Created on 9/25/2018 1:46 PM.
 */
public final class CredentialsAsFirstParameterResourceResolver implements AuditResourceResolver {
    public CredentialsAsFirstParameterResourceResolver() {
    }

    @Override
    public String[] resolveFrom(JoinPoint joinPoint, Object retval) {
        return toResources(AopUtils.unWrapJoinPoint(joinPoint).getArgs());
    }

    @Override
    public String[] resolveFrom(JoinPoint joinPoint, Exception exception) {
        return toResources(AopUtils.unWrapJoinPoint(joinPoint).getArgs());
    }

    private static String[] toResources(Object[] args) {
        return new String[]{"supplied credentials: " + Arrays.asList((Object[])((Object[])args[0]))};
    }
}
