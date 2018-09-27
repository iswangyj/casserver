package org.jasig.cas.authentication.handler;

import org.jasig.cas.authentication.RootCasException;
import org.springframework.util.Assert;

/**
 * @author SxL
 * Created on 9/25/2018 2:11 PM.
 */
public abstract class AuthenticationException extends RootCasException {
    private static final long serialVersionUID = 3906648604830611762L;
    private String type;

    public AuthenticationException(String code) {
        super(code);
        this.type = "error";
    }

    public AuthenticationException(String code, String msg) {
        super(code, msg);
        this.type = "error";
    }

    public AuthenticationException(String code, String msg, String type) {
        this(code, msg);
        Assert.hasLength(type, "The exception type cannot be blank");
        this.type = type;
    }

    public AuthenticationException(String code, Throwable throwable) {
        super(code, throwable);
        this.type = "error";
    }

    public final String getType() {
        return this.type;
    }
}
