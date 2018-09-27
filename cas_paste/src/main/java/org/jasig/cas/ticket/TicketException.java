package org.jasig.cas.ticket;

import org.jasig.cas.authentication.RootCasException;

/**
 * @author SxL
 * Created on 9/25/2018 3:35 PM.
 */
public abstract class TicketException extends RootCasException {
    private static final long serialVersionUID = -5128676415951733624L;

    public TicketException(String code, Throwable throwable) {
        super(code, throwable);
    }

    public TicketException(String code) {
        super(code);
    }

    public TicketException(String code, String msg) {
        super(code, msg);
    }
}

