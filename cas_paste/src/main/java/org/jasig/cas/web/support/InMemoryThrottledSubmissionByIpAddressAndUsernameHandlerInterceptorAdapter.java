package org.jasig.cas.web.support;

import com.github.inspektr.common.web.ClientInfoHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * @author SxL
 * Created on 9/25/2018 5:55 PM.
 */
public final class InMemoryThrottledSubmissionByIpAddressAndUsernameHandlerInterceptorAdapter extends AbstractInMemoryThrottledSubmissionHandlerInterceptorAdapter {
    public InMemoryThrottledSubmissionByIpAddressAndUsernameHandlerInterceptorAdapter() {
    }

    @Override
    protected String constructKey(HttpServletRequest request) {
        String username = request.getParameter(this.getUsernameParameter());
        return username == null ? request.getRemoteAddr() : ClientInfoHolder.getClientInfo().getClientIpAddress() + ";" + username.toLowerCase();
    }
}

