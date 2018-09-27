package org.jasig.cas.web.flow;

import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.execution.FlowExecutionListenerAdapter;
import org.springframework.webflow.execution.FlowSession;
import org.springframework.webflow.execution.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Min;

/**
 * @author SxL
 * Created on 9/25/2018 5:48 PM.
 */
public final class TerminateWebSessionListener extends FlowExecutionListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TerminateWebSessionListener.class);
    @Min(0L)
    private int timeToDieInSeconds = 2;

    public TerminateWebSessionListener() {
    }

    @Override
    public void sessionEnded(RequestContext context, FlowSession session, String outcome, AttributeMap output) {
        if (session.isRoot()) {
            HttpServletRequest request = WebUtils.getHttpServletRequest(context);
            HttpSession webSession = request.getSession(false);
            if (webSession != null) {
                LOGGER.debug("Terminate web session {} in {} seconds", webSession.getId(), this.timeToDieInSeconds);
                webSession.setMaxInactiveInterval(this.timeToDieInSeconds);
            }
        }

    }

    public int getTimeToDieInSeconds() {
        return this.timeToDieInSeconds;
    }

    public void setTimeToDieInSeconds(int timeToDieInSeconds) {
        this.timeToDieInSeconds = timeToDieInSeconds;
    }
}
