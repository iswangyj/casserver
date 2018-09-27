package org.jasig.cas.web;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.authentication.principal.SimpleWebApplicationServiceImpl;
import org.jasig.cas.services.UnauthorizedServiceException;
import org.jasig.cas.ticket.TicketException;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

/**
 * @author SxL
 * Created on 9/25/2018 5:22 PM.
 */
public final class ProxyController extends AbstractController {
    private static final String CONST_PROXY_FAILURE = "cas2ProxyFailureView";
    private static final String CONST_PROXY_SUCCESS = "cas2ProxySuccessView";
    private static final String MODEL_SERVICE_TICKET = "ticket";
    @NotNull
    private CentralAuthenticationService centralAuthenticationService;

    public ProxyController() {
        this.setCacheSeconds(0);
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ticket = request.getParameter("pgt");
        Service targetService = this.getTargetService(request);
        if (StringUtils.hasText(ticket) && targetService != null) {
            try {
                return new ModelAndView("cas2ProxySuccessView", "ticket", this.centralAuthenticationService.grantServiceTicket(ticket, targetService));
            } catch (TicketException var6) {
                return this.generateErrorView(var6.getCode(), var6.getCode(), new Object[]{ticket});
            } catch (UnauthorizedServiceException var7) {
                return this.generateErrorView("UNAUTHORIZED_SERVICE", "UNAUTHORIZED_SERVICE_PROXY", new Object[]{targetService});
            }
        } else {
            return this.generateErrorView("INVALID_REQUEST", "INVALID_REQUEST_PROXY", (Object[])null);
        }
    }

    private Service getTargetService(HttpServletRequest request) {
        return SimpleWebApplicationServiceImpl.createServiceFrom(request);
    }

    private ModelAndView generateErrorView(String code, String description, Object[] args) {
        ModelAndView modelAndView = new ModelAndView("cas2ProxyFailureView");
        modelAndView.addObject("code", code);
        modelAndView.addObject("description", this.getMessageSourceAccessor().getMessage(description, args, description));
        return modelAndView;
    }

    public void setCentralAuthenticationService(CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }
}
