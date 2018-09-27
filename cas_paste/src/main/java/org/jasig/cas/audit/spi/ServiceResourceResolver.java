package org.jasig.cas.audit.spi;

import com.github.inspektr.audit.spi.AuditResourceResolver;
import org.aspectj.lang.JoinPoint;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.util.AopUtils;

/**
 * @author SxL
 * Created on 9/25/2018 1:47 PM.
 */
public final class ServiceResourceResolver implements AuditResourceResolver {
    public ServiceResourceResolver() {
    }

    @Override
    public String[] resolveFrom(JoinPoint joinPoint, Object retval) {
        Service service = (Service)AopUtils.unWrapJoinPoint(joinPoint).getArgs()[1];
        return new String[]{retval.toString() + " for " + service.getId()};
    }

    @Override
    public String[] resolveFrom(JoinPoint joinPoint, Exception ex) {
        Service service = (Service)AopUtils.unWrapJoinPoint(joinPoint).getArgs()[1];
        return new String[]{service.getId()};
    }
}
