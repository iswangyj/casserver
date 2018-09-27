package org.jasig.cas.ticket.proxy.support;

import org.jasig.cas.authentication.Credential;
import org.jasig.cas.ticket.proxy.ProxyHandler;

/**
 * @author SxL
 * Created on 9/25/2018 3:39 PM.
 */
public final class Cas10ProxyHandler implements ProxyHandler {
    public Cas10ProxyHandler() {
    }

    @Override
    public String handle(Credential credential, String proxyGrantingTicketId) {
        return null;
    }

    @Override
    public boolean canHandle(Credential credential) {
        return false;
    }
}
