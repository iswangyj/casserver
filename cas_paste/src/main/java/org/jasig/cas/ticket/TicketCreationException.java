package org.jasig.cas.ticket;

/**
 * @author SxL
 * Created on 9/25/2018 3:35 PM.
 */
public class TicketCreationException extends TicketException {
    private static final long serialVersionUID = 5501212207531289993L;
    private static final String CODE = "CREATION_ERROR";

    public TicketCreationException() {
        super("CREATION_ERROR");
    }

    public TicketCreationException(Throwable throwable) {
        super("CREATION_ERROR", throwable);
    }
}
