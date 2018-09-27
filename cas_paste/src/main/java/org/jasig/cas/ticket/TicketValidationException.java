package org.jasig.cas.ticket;

import org.jasig.cas.authentication.principal.Service;

/**
 * @author SxL
 * Created on 9/25/2018 3:38 PM.
 */
public class TicketValidationException extends TicketException {
    private static final long serialVersionUID = 3257004341537093175L;
    private static final String CODE = "INVALID_SERVICE";
    private final Service service;

    public TicketValidationException(Service service) {
        super("INVALID_SERVICE");
        this.service = service;
    }

    public Service getOriginalService() {
        return this.service;
    }
}
