package org.jasig.cas.authentication;

import java.security.GeneralSecurityException;

/**
 * @author SxL
 * Created on 9/25/2018 1:55 PM.
 */
public interface AuthenticationHandler {
    HandlerResult authenticate(Credential var1) throws GeneralSecurityException, PreventedException;

    boolean supports(Credential var1);

    String getName();
}
