package org.jasig.cas.authentication;

import java.util.Map;

/**
 * @author SxL
 * Created on 9/25/2018 2:05 PM.
 */
public class PrincipalException extends AuthenticationException {
    private static final long serialVersionUID = -6590363469748313596L;

    public PrincipalException(String message, Map<String, Class<? extends Exception>> handlerErrors, Map<String, HandlerResult> handlerSuccesses) {
        super(message, handlerErrors, handlerSuccesses);
    }
}
