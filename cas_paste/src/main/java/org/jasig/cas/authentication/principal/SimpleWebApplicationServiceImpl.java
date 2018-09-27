package org.jasig.cas.authentication.principal;

import org.jasig.cas.services.ume.CasServerCfgPub;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author SxL
 * Created on 9/25/2018 2:49 PM.
 */
public final class SimpleWebApplicationServiceImpl extends AbstractWebApplicationService {
    private static final String CONST_PARAM_SERVICE = "service";
    private static final String CONST_PARAM_TARGET_SERVICE = "targetService";
    private static final String CONST_PARAM_TICKET = "ticket";
    private static final String CONST_PARAM_METHOD = "method";
    private final Response.ResponseType responseType;
    private static final long serialVersionUID = 8334068957483758042L;

    public SimpleWebApplicationServiceImpl(String id) {
        this(id, id, (String)null, (Response.ResponseType)null);
    }

    private SimpleWebApplicationServiceImpl(String id, String originalUrl, String artifactId, Response.ResponseType responseType) {
        super(id, originalUrl, artifactId);
        this.responseType = responseType;
    }

    public static SimpleWebApplicationServiceImpl createServiceFrom(HttpServletRequest request) {
        String targetService = request.getParameter("targetService");
        String method = request.getParameter("method");
        String serviceToUse = StringUtils.hasText(targetService) ? targetService : request.getParameter("service");
        if (!StringUtils.hasText(serviceToUse)) {
            serviceToUse = CasServerCfgPub.getPortalPage();
            if (!StringUtils.hasText(serviceToUse)) {
                return null;
            }
        }

        String id = cleanupUrl(serviceToUse);
        String artifactId = request.getParameter("ticket");
        return new SimpleWebApplicationServiceImpl(id, serviceToUse, artifactId, "POST".equals(method) ? Response.ResponseType.POST : Response.ResponseType.REDIRECT);
    }

    @Override
    public Response getResponse(String ticketId) {
        Map<String, String> parameters = new HashMap();
        if (StringUtils.hasText(ticketId)) {
            parameters.put("ticket", ticketId);
        }

        return Response.ResponseType.POST == this.responseType ? Response.getPostResponse(this.getOriginalUrl(), parameters) : Response.getRedirectResponse(this.getOriginalUrl(), parameters);
    }
}
