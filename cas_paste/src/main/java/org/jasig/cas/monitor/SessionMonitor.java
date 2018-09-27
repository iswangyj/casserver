package org.jasig.cas.monitor;

import javax.validation.constraints.NotNull;

/**
 * @author SxL
 * Created on 9/25/2018 3:01 PM.
 */
public class SessionMonitor implements Monitor<SessionStatus> {
    @NotNull
    private TicketRegistryState registryState;
    private int sessionCountWarnThreshold = -1;
    private int serviceTicketCountWarnThreshold = -1;

    public SessionMonitor() {
    }

    public void setTicketRegistry(TicketRegistryState state) {
        this.registryState = state;
    }

    public void setSessionCountWarnThreshold(int threshold) {
        this.sessionCountWarnThreshold = threshold;
    }

    public void setServiceTicketCountWarnThreshold(int threshold) {
        this.serviceTicketCountWarnThreshold = threshold;
    }

    @Override
    public String getName() {
        return SessionMonitor.class.getSimpleName();
    }

    @Override
    public SessionStatus observe() {
        try {
            int sessionCount = this.registryState.sessionCount();
            int ticketCount = this.registryState.serviceTicketCount();
            if (sessionCount != -2147483648 && ticketCount != -2147483648) {
                StringBuilder msg = new StringBuilder();
                StatusCode code = StatusCode.OK;
                if (this.sessionCountWarnThreshold > -1 && sessionCount > this.sessionCountWarnThreshold) {
                    code = StatusCode.WARN;
                    msg.append(String.format("Session count (%s) is above threshold %s. ", sessionCount, this.sessionCountWarnThreshold));
                } else {
                    msg.append(sessionCount).append(" sessions. ");
                }

                if (this.serviceTicketCountWarnThreshold > -1 && ticketCount > this.serviceTicketCountWarnThreshold) {
                    code = StatusCode.WARN;
                    msg.append(String.format("Service ticket count (%s) is above threshold %s.", ticketCount, this.serviceTicketCountWarnThreshold));
                } else {
                    msg.append(ticketCount).append(" service tickets.");
                }

                return new SessionStatus(code, msg.toString(), sessionCount, ticketCount);
            } else {
                return new SessionStatus(StatusCode.UNKNOWN, String.format("Ticket registry %s reports unknown session and/or ticket counts.", this.registryState.getClass().getName()), sessionCount, ticketCount);
            }
        } catch (Exception var5) {
            return new SessionStatus(StatusCode.ERROR, var5.getMessage());
        }
    }
}
