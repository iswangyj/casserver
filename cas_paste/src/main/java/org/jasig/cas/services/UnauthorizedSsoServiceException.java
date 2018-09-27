package org.jasig.cas.services;

/**
 * @author SxL
 * Created on 9/25/2018 3:21 PM.
 */
public class UnauthorizedSsoServiceException extends UnauthorizedServiceException {
    private static final long serialVersionUID = 8909291297815558561L;
    private static final String CODE = "service.not.authorized.sso";

    public UnauthorizedSsoServiceException() {
        this("service.not.authorized.sso");
    }

    public UnauthorizedSsoServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthorizedSsoServiceException(String message) {
        super(message);
    }
}
