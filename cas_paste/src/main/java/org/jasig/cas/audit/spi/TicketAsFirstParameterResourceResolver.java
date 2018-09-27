package org.jasig.cas.audit.spi;

import com.github.inspektr.audit.spi.AuditResourceResolver;
import org.aspectj.lang.JoinPoint;
import org.jasig.cas.util.AopUtils;

/**
 * @author SxL
 * Created on 9/25/2018 1:48 PM.
 */
public final class TicketAsFirstParameterResourceResolver implements AuditResourceResolver {
    public TicketAsFirstParameterResourceResolver() {
    }

    @Override
    public String[] resolveFrom(JoinPoint joinPoint, Exception exception) {
        return new String[]{AopUtils.unWrapJoinPoint(joinPoint).getArgs()[0].toString()};
    }

    @Override
    public String[] resolveFrom(JoinPoint joinPoint, Object object) {
        return new String[]{AopUtils.unWrapJoinPoint(joinPoint).getArgs()[0].toString()};
    }
}
