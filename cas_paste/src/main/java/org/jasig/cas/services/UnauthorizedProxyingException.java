package org.jasig.cas.services;

/**
 * @author SxL
 * Created on 9/25/2018 3:20 PM.
 */
public class UnauthorizedProxyingException extends UnauthorizedServiceException {
    private static final long serialVersionUID = -7307803750894078575L;
    public static final String CODE = "UNAUTHORIZED_SERVICE_PROXY";

    public UnauthorizedProxyingException() {
        super("UNAUTHORIZED_SERVICE_PROXY");
    }

    public UnauthorizedProxyingException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthorizedProxyingException(String message) {
        super(message);
    }
}
