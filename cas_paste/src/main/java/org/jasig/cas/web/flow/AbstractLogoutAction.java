package org.jasig.cas.web.flow;

import org.jasig.cas.web.support.WebUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author SxL
 * Created on 9/25/2018 5:36 PM.
 */
public abstract class AbstractLogoutAction extends AbstractAction {
    public static final String LOGOUT_INDEX = "logoutIndex";
    public static final String FINISH_EVENT = "finish";
    public static final String FRONT_EVENT = "front";
    public static final String REDIRECT_APP_EVENT = "redirectApp";

    public AbstractLogoutAction() {
    }

    @Override
    protected final Event doExecute(RequestContext context) throws Exception {
        HttpServletRequest request = WebUtils.getHttpServletRequest(context);
        HttpServletResponse response = WebUtils.getHttpServletResponse(context);
        this.preventCaching(response);
        return this.doInternalExecute(request, response, context);
    }

    protected abstract Event doInternalExecute(HttpServletRequest var1, HttpServletResponse var2, RequestContext var3) throws Exception;

    protected final void preventCaching(HttpServletResponse response) {
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 1L);
        response.setHeader("Cache-Control", "no-cache");
        response.addHeader("Cache-Control", "no-store");
    }

    protected final void putLogoutIndex(RequestContext context, int index) {
        context.getFlowScope().put("logoutIndex", index);
    }

    protected final int getLogoutIndex(RequestContext context) {
        Object value = context.getFlowScope().get("logoutIndex");
        return value == null ? 0 : (Integer)value;
    }
}
