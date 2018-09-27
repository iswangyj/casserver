package org.jasig.cas.ticket.proxy;

import org.jasig.cas.authentication.Credential;

/**
 * @author SxL
 * Created on 9/25/2018 3:39 PM.
 */
public interface ProxyHandler {
    String handle(Credential var1, String var2);

    boolean canHandle(Credential var1);
}
