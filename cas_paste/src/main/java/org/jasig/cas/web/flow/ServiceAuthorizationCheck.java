package org.jasig.cas.web.flow;

import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.services.RegisteredService;
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
 * Created on 9/25/2018 5:47 PM.
 */
public final class ServiceAuthorizationCheck extends AbstractAction {
    @NotNull
    private final ServicesManager servicesManager;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ServiceAuthorizationCheck(ServicesManager servicesManager) {
        this.servicesManager = servicesManager;
    }

    @Override
    protected Event doExecute(RequestContext context) throws Exception {
        Service service = WebUtils.getService(context);
        if (service == null) {
            return this.success();
        } else if (this.servicesManager.getAllServices().size() == 0) {
            String msg = String.format("No service definitions are found in the service manager. Service [%s] will not be automatically authorized to request authentication.", service.getId());
            this.logger.warn(msg);
            throw new UnauthorizedServiceException("screen.service.empty.error.message");
        } else {
            RegisteredService registeredService = this.servicesManager.findServiceBy(service);
            String msg;
            if (registeredService == null) {
                msg = String.format("ServiceManagement: Unauthorized Service Access. Service [%s] is not found in service registry.", service.getId());
                this.logger.warn(msg);
                throw new UnauthorizedServiceException("screen.service.error.message", msg);
            } else if (!registeredService.isEnabled()) {
                msg = String.format("ServiceManagement: Unauthorized Service Access. Service %s] is not enabled in service registry.", service.getId());
                this.logger.warn(msg);
                throw new UnauthorizedServiceException("screen.service.error.message", msg);
            } else {
                return this.success();
            }
        }
    }
}

