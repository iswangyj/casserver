package org.jasig.cas.web.support;

import com.github.inspektr.common.web.ClientInfoHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * @author SxL
 * Created on 9/25/2018 5:55 PM.
 */
public final class InMemoryThrottledSubmissionByIpAddressHandlerInterceptorAdapter extends AbstractInMemoryThrottledSubmissionHandlerInterceptorAdapter {
    public InMemoryThrottledSubmissionByIpAddressHandlerInterceptorAdapter() {
    }

    @Override
    protected String constructKey(HttpServletRequest request) {
        return ClientInfoHolder.getClientInfo().getClientIpAddress();
    }
}

