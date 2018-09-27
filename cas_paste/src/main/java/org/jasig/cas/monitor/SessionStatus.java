package org.jasig.cas.monitor;

/**
 * @author SxL
 * Created on 9/25/2018 3:02 PM.
 */
public class SessionStatus extends Status {
    private final int sessionCount;
    private final int serviceTicketCount;

    public SessionStatus(StatusCode code, String desc) {
        this(code, desc, 0, 0);
    }

    public SessionStatus(StatusCode code, String desc, int sessions, int serviceTickets) {
        super(code, desc);
        this.sessionCount = sessions;
        this.serviceTicketCount = serviceTickets;
    }

    public int getSessionCount() {
        return this.sessionCount;
    }

    public int getServiceTicketCount() {
        return this.serviceTicketCount;
    }
}
