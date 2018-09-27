package org.jasig.cas.logout;

import org.apache.commons.codec.binary.Base64;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.authentication.principal.SingleLogoutService;
import org.jasig.cas.services.LogoutType;
import org.jasig.cas.services.RegisteredService;
import org.jasig.cas.services.ServicesManager;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.util.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;

/**
 * @author SxL
 * Created on 9/25/2018 2:52 PM.
 */
public final class LogoutManagerImpl implements LogoutManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogoutManagerImpl.class);
    private static final Charset ASCII = Charset.forName("ASCII");
    @NotNull
    private final ServicesManager servicesManager;
    @NotNull
    private final HttpClient httpClient;
    @NotNull
    private final LogoutMessageCreator logoutMessageBuilder;
    private boolean disableSingleSignOut = false;

    public LogoutManagerImpl(ServicesManager servicesManager, HttpClient httpClient, LogoutMessageCreator logoutMessageBuilder) {
        this.servicesManager = servicesManager;
        this.httpClient = httpClient;
        this.logoutMessageBuilder = logoutMessageBuilder;
    }

    @Override
    public List<LogoutRequest> performLogout(TicketGrantingTicket ticket) {
        Map services;
        synchronized(ticket) {
            services = ticket.getServices();
            ticket.removeAllServices();
        }

        ticket.markTicketExpired();
        List<LogoutRequest> logoutRequests = new ArrayList();
        if (!this.disableSingleSignOut) {
            Iterator var4 = services.keySet().iterator();

            while(true) {
                SingleLogoutService singleLogoutService;
                LogoutRequest logoutRequest;
                RegisteredService registeredService;
                do {
                    String ticketId;
                    Service service;
                    do {
                        do {
                            if (!var4.hasNext()) {
                                return logoutRequests;
                            }

                            ticketId = (String)var4.next();
                            service = (Service)services.get(ticketId);
                        } while(!(service instanceof SingleLogoutService));

                        singleLogoutService = (SingleLogoutService)service;
                    } while(singleLogoutService.isLoggedOutAlready());

                    logoutRequest = new LogoutRequest(ticketId, singleLogoutService);
                    logoutRequests.add(logoutRequest);
                    registeredService = this.servicesManager.findServiceBy(service);
                } while(registeredService != null && registeredService.getLogoutType() != null && registeredService.getLogoutType() != LogoutType.BACK_CHANNEL);

                if (this.performBackChannelLogout(logoutRequest)) {
                    logoutRequest.setStatus(LogoutRequestStatus.SUCCESS);
                } else {
                    logoutRequest.setStatus(LogoutRequestStatus.FAILURE);
                    LOGGER.warn("Logout message not sent to [{}]; Continuing processing...", singleLogoutService.getId());
                }
            }
        } else {
            return logoutRequests;
        }
    }

    private boolean performBackChannelLogout(LogoutRequest request) {
        String logoutRequest = this.logoutMessageBuilder.create(request);
        request.getService().setLoggedOutAlready(true);
        LOGGER.debug("Sending logout request for: [{}]", request.getService().getId());
        return this.httpClient.sendMessageToEndPoint(request.getService().getOriginalUrl(), logoutRequest, true);
    }

    @Override
    public String createFrontChannelLogoutMessage(LogoutRequest logoutRequest) {
        String logoutMessage = this.logoutMessageBuilder.create(logoutRequest);
        Deflater deflater = new Deflater();
        deflater.setInput(logoutMessage.getBytes(ASCII));
        deflater.finish();
        byte[] buffer = new byte[logoutMessage.length()];
        int resultSize = deflater.deflate(buffer);
        byte[] output = new byte[resultSize];
        System.arraycopy(buffer, 0, output, 0, resultSize);
        return Base64.encodeBase64String(output);
    }

    public void setDisableSingleSignOut(boolean disableSingleSignOut) {
        this.disableSingleSignOut = disableSingleSignOut;
    }
}
