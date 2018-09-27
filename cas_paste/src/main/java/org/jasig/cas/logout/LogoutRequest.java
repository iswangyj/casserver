package org.jasig.cas.logout;

import org.jasig.cas.authentication.principal.SingleLogoutService;

import java.io.Serializable;

/**
 * @author SxL
 * Created on 9/25/2018 2:54 PM.
 */
public final class LogoutRequest implements Serializable {
    private static final long serialVersionUID = -6411421298859045022L;
    private final String ticketId;
    private final SingleLogoutService service;
    private LogoutRequestStatus status;

    public LogoutRequest(String ticketId, SingleLogoutService service) {
        this.status = LogoutRequestStatus.NOT_ATTEMPTED;
        this.ticketId = ticketId;
        this.service = service;
    }

    public LogoutRequestStatus getStatus() {
        return this.status;
    }

    public void setStatus(LogoutRequestStatus status) {
        this.status = status;
    }

    public String getTicketId() {
        return this.ticketId;
    }

    public SingleLogoutService getService() {
        return this.service;
    }
}
