package org.jasig.cas.web.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.webflow.execution.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author SxL
 * Created on 9/25/2018 5:54 PM.
 */
public abstract class AbstractThrottledSubmissionHandlerInterceptorAdapter extends HandlerInterceptorAdapter implements InitializingBean {
    private static final int DEFAULT_FAILURE_THRESHOLD = 100;
    private static final int DEFAULT_FAILURE_RANGE_IN_SECONDS = 60;
    private static final String DEFAULT_USERNAME_PARAMETER = "username";
    private static final String SUCCESSFUL_AUTHENTICATION_EVENT = "success";
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Min(0L)
    private int failureThreshold = 100;
    @Min(0L)
    private int failureRangeInSeconds = 60;
    @NotNull
    private String usernameParameter = "username";
    private double thresholdRate;

    public AbstractThrottledSubmissionHandlerInterceptorAdapter() {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.thresholdRate = (double)this.failureThreshold / (double)this.failureRangeInSeconds;
    }

    @Override
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        if (!"POST".equals(request.getMethod())) {
            return true;
        } else if (this.exceedsThreshold(request)) {
            this.recordThrottle(request);
            response.sendError(403, "Access Denied for user [" + request.getParameter(this.usernameParameter) + " from IP Address [" + request.getRemoteAddr() + "]");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public final void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        if ("POST".equals(request.getMethod())) {
            RequestContext context = (RequestContext)request.getAttribute("flowRequestContext");
            if (context != null && context.getCurrentEvent() != null) {
                if (!"success".equals(context.getCurrentEvent().getId())) {
                    this.recordSubmissionFailure(request);
                }
            }
        }
    }

    public final void setFailureThreshold(int failureThreshold) {
        this.failureThreshold = failureThreshold;
    }

    public final void setFailureRangeInSeconds(int failureRangeInSeconds) {
        this.failureRangeInSeconds = failureRangeInSeconds;
    }

    public final void setUsernameParameter(String usernameParameter) {
        this.usernameParameter = usernameParameter;
    }

    protected double getThresholdRate() {
        return this.thresholdRate;
    }

    protected int getFailureThreshold() {
        return this.failureThreshold;
    }

    protected int getFailureRangeInSeconds() {
        return this.failureRangeInSeconds;
    }

    protected String getUsernameParameter() {
        return this.usernameParameter;
    }

    protected void recordThrottle(HttpServletRequest request) {
        this.logger.warn("Throttling submission from {}.  More than {} failed login attempts within {} seconds.", new Object[]{request.getRemoteAddr(), this.failureThreshold, this.failureRangeInSeconds});
    }

    protected abstract void recordSubmissionFailure(HttpServletRequest var1);

    protected abstract boolean exceedsThreshold(HttpServletRequest var1);
}
