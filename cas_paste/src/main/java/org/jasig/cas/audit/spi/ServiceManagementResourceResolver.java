package org.jasig.cas.audit.spi;

import com.github.inspektr.audit.spi.AuditResourceResolver;
import org.aspectj.lang.JoinPoint;
import org.jasig.cas.util.AopUtils;

/**
 * @author SxL
 * Created on 9/25/2018 1:47 PM.
 */
public final class ServiceManagementResourceResolver implements AuditResourceResolver {
    public ServiceManagementResourceResolver() {
    }

    @Override
    public String[] resolveFrom(JoinPoint target, Object returnValue) {
        return this.findService(target);
    }

    @Override
    public String[] resolveFrom(JoinPoint target, Exception exception) {
        return this.findService(target);
    }

    private String[] findService(JoinPoint joinPoint) {
        JoinPoint j = AopUtils.unWrapJoinPoint(joinPoint);
        Long id = (Long)j.getArgs()[0];
        return id == null ? new String[]{""} : new String[]{"id=" + id};
    }
}
