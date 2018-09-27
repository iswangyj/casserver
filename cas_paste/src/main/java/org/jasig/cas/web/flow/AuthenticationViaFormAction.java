package org.jasig.cas.web.flow;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.Message;
import org.jasig.cas.authentication.AuthenticationException;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.services.LogoutType;
import org.jasig.cas.services.RegisteredServiceImpl;
import org.jasig.cas.ticket.TicketCreationException;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.jasig.cas.web.bind.CredentialsBinder;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.util.StringUtils;
import org.springframework.web.util.CookieGenerator;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Iterator;
import java.util.Map;

/**
 * @author SxL
 * Created on 9/25/2018 5:38 PM.
 */
public class AuthenticationViaFormAction {
    public static final String SUCCESS = "success";
    public static final String SUCCESS_WITH_WARNINGS = "successWithWarnings";
    public static final String WARN = "warn";
    public static final String AUTHENTICATION_FAILURE = "authenticationFailure";
    public static final String ERROR = "error";
    private CredentialsBinder credentialsBinder;
    @NotNull
    private CentralAuthenticationService centralAuthenticationService;
    @NotNull
    private TicketRegistry ticketRegistry;
    @NotNull
    private CookieGenerator warnCookieGenerator;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public AuthenticationViaFormAction() {
    }

    public final void doBind(RequestContext context, Credential credential) throws Exception {
        HttpServletRequest request = WebUtils.getHttpServletRequest(context);
        if (this.credentialsBinder != null && this.credentialsBinder.supports(credential.getClass())) {
            this.credentialsBinder.bind(request, credential);
        }

    }

    public final Event submit(RequestContext context, Credential credential, MessageContext messageContext) throws Exception {
        String authoritativeLoginTicket = WebUtils.getLoginTicketFromFlowScope(context);
        String providedLoginTicket = WebUtils.getLoginTicketFromRequest(context);
        HttpServletRequest request = WebUtils.getHttpServletRequest(context);
        request.getSession().setAttribute("umeNewFlag", "true");
        if (!authoritativeLoginTicket.equals(providedLoginTicket)) {
            this.logger.warn("Invalid login ticket {}", providedLoginTicket);
            messageContext.addMessage((new MessageBuilder()).error().code("error.invalid.loginticket").build());
            return this.newEvent("error");
        } else {
            String ticketGrantingTicketId = WebUtils.getTicketGrantingTicketId(context);
            Service service = WebUtils.getService(context);
            UsernamePasswordCredential upc = null;
            String tgtId;
            if (credential instanceof UsernamePasswordCredential) {
                upc = (UsernamePasswordCredential)credential;
                tgtId = context.getRequestParameters().get("bSpecial");
                upc.setbSpecial(tgtId);
                String strCA = context.getRequestParameters().get("strCA");
                upc.setStrCA(strCA);

                if (service == null) {
                    RegisteredServiceImpl registeredService = new RegisteredServiceImpl();

                    registeredService.setServiceId("http://localhost:8080/**");
                    registeredService.setAllowedToProxy(true);
                    registeredService.setAnonymousAccess(false);
                    registeredService.setEnabled(true);
                    registeredService.setEvaluationOrder(10000);
                    registeredService.setId(1);
                    registeredService.setName("天津橙子科技.门户系统");
                    registeredService.setSsoEnabled(true);
                    registeredService.setLogoutType(LogoutType.BACK_CHANNEL);

                    upc.setService(registeredService.toString());
                } else {
                    upc.setService(service.toString());
                }
            }

            if (StringUtils.hasText(context.getRequestParameters().get("renew")) && ticketGrantingTicketId != null && service != null) {
                try {
                    tgtId = this.centralAuthenticationService.grantServiceTicket(ticketGrantingTicketId, service, new Credential[]{credential});
                    WebUtils.putServiceTicketInRequestScope(context, tgtId);
                    this.putWarnCookieIfRequestParameterPresent(context);
                    return this.newEvent("warn");
                } catch (AuthenticationException var14) {
                    return this.newEvent("authenticationFailure", var14);
                } catch (TicketCreationException var15) {
                    this.logger.warn("Invalid attempt to access service using renew=true with different credential. Ending SSO session.");
                    this.centralAuthenticationService.destroyTicketGrantingTicket(ticketGrantingTicketId);
                } catch (TicketException var16) {
                    return this.newEvent("error", var16);
                }
            }

            try {
                tgtId = this.centralAuthenticationService.createTicketGrantingTicket(new Credential[]{credential});
                WebUtils.putTicketGrantingTicketInFlowScope(context, tgtId);
                this.putWarnCookieIfRequestParameterPresent(context);
                TicketGrantingTicket tgt = (TicketGrantingTicket)this.ticketRegistry.getTicket(tgtId);
                if (this.addWarningMessagesToMessageContextIfNeeded(tgt, messageContext)) {
                    return this.newEvent("successWithWarnings");
                } else {
                    request.getSession().removeAttribute("umeNewFlag");
                    return this.newEvent("success");
                }
            } catch (AuthenticationException var12) {
                var12.printStackTrace();
                return this.newEvent("authenticationFailure", var12);
            } catch (Exception var13) {
                var13.printStackTrace();
                return this.newEvent("error", var13);
            }
        }
    }

    private boolean addWarningMessagesToMessageContextIfNeeded(TicketGrantingTicket tgtId, MessageContext messageContext) {
        boolean foundAndAddedWarnings = false;
        Iterator var4 = tgtId.getAuthentication().getSuccesses().entrySet().iterator();

        while(var4.hasNext()) {
            Map.Entry<String, HandlerResult> entry = (Map.Entry)var4.next();

            for(Iterator var6 = ((HandlerResult)entry.getValue()).getWarnings().iterator(); var6.hasNext(); foundAndAddedWarnings = true) {
                Message message = (Message)var6.next();
                this.addWarningToContext(messageContext, message);
            }
        }

        return foundAndAddedWarnings;
    }

    private void putWarnCookieIfRequestParameterPresent(RequestContext context) {
        HttpServletResponse response = WebUtils.getHttpServletResponse(context);
        if (StringUtils.hasText(context.getExternalContext().getRequestParameterMap().get("warn"))) {
            this.warnCookieGenerator.addCookie(response, "true");
        } else {
            this.warnCookieGenerator.removeCookie(response);
        }

    }

    private AuthenticationException getAuthenticationExceptionAsCause(TicketException e) {
        return (AuthenticationException)e.getCause();
    }

    private Event newEvent(String id) {
        return new Event(this, id);
    }

    private Event newEvent(String id, Exception error) {
        return new Event(this, id, new LocalAttributeMap("error", error));
    }

    public final void setCentralAuthenticationService(CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }

    public void setTicketRegistry(TicketRegistry ticketRegistry) {
        this.ticketRegistry = ticketRegistry;
    }

    public final void setCredentialsBinder(CredentialsBinder credentialsBinder) {
        this.credentialsBinder = credentialsBinder;
    }

    public final void setWarnCookieGenerator(CookieGenerator warnCookieGenerator) {
        this.warnCookieGenerator = warnCookieGenerator;
    }

    private void addWarningToContext(MessageContext context, Message warning) {
        MessageBuilder builder = (new MessageBuilder()).warning().code(warning.getCode()).defaultText(warning.getDefaultMessage()).args(warning.getParams());
        context.addMessage(builder.build());
    }

}
