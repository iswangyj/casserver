package org.jasig.cas.ticket;

/**
 * @author SxL
 * Created on 9/25/2018 3:33 PM.
 */
public class InvalidTicketException extends TicketException {
    private static final long serialVersionUID = 9141891414482490L;
    private String ticketId = null;
    private static final String CODE = "INVALID_TICKET";

    public InvalidTicketException(String ticketId) {
        super("INVALID_TICKET");
        this.ticketId = ticketId;
    }

    public InvalidTicketException(Throwable throwable, String ticketId) {
        super("INVALID_TICKET", throwable);
    }

    public String getMessage() {
        return this.ticketId;
    }
}
