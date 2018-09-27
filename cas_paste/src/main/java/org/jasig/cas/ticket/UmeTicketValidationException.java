package org.jasig.cas.ticket;

import org.jasig.cas.authentication.principal.Service;

/**
 * @author SxL
 * Created on 9/25/2018 3:38 PM.
 */
public class UmeTicketValidationException extends TicketException {
    private Service service;

    public UmeTicketValidationException(String code, String msg, Service service) {
        super(code, msg);
        this.service = service;
    }

    public Service getOriginalService() {
        return this.service;
    }
}
