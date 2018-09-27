package org.jasig.cas.services;

/**
 * @author SxL
 * Created on 9/25/2018 3:21 PM.
 */
public class UnauthorizedServiceException extends RuntimeException {
    private static final long serialVersionUID = 3905807495715960369L;
    public static final String CODE_UNAUTHZ_SERVICE = "screen.service.error.message";
    public static final String CODE_EMPTY_SVC_MGMR = "screen.service.empty.error.message";
    private String code;

    public UnauthorizedServiceException(String message) {
        super(message);
        this.code = null;
    }

    public UnauthorizedServiceException(String code, String message) {
        this(message);
        this.code = code;
    }

    public UnauthorizedServiceException(String message, Throwable cause) {
        super(message, cause);
        this.code = null;
    }

    public final String getCode() {
        return this.code;
    }
}
