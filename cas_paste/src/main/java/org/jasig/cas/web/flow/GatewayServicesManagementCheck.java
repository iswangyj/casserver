package org.jasig.cas.web.flow;

import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.services.ServicesManager;
import org.jasig.cas.services.UnauthorizedServiceException;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import javax.validation.constraints.NotNull;

/**
 * @author SxL
 * Created on 9/25/2018 5:43 PM.
 */
public class GatewayServicesManagementCheck extends AbstractAction {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @NotNull
    private final ServicesManager servicesManager;

    public GatewayServicesManagementCheck(ServicesManager servicesManager) {
        this.servicesManager = servicesManager;
    }

    @Override
    protected Event doExecute(RequestContext context) throws Exception {
        Service service = WebUtils.getService(context);
        boolean match = this.servicesManager.matchesExistingService(service);
        if (match) {
            return this.success();
        } else {
            String msg = String.format("ServiceManagement: Unauthorized Service Access. Service [%s] does not match entries in service registry.", service.getId());
            this.logger.warn(msg);
            throw new UnauthorizedServiceException("screen.service.error.message", msg);
        }
    }
}
