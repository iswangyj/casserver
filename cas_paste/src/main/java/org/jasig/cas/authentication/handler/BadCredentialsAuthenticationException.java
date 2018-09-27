package org.jasig.cas.authentication.handler;

/**
 * @author SxL
 * Created on 9/25/2018 2:12 PM.
 */
public class BadCredentialsAuthenticationException extends AuthenticationException {
    public static final BadCredentialsAuthenticationException ERROR = new BadCredentialsAuthenticationException();
    private static final long serialVersionUID = 3256719585087797044L;
    public static final String CODE = "error.authentication.credentials.bad";

    public BadCredentialsAuthenticationException() {
        super("error.authentication.credentials.bad");
    }

    public BadCredentialsAuthenticationException(Throwable throwable) {
        super("error.authentication.credentials.bad", throwable);
    }

    public BadCredentialsAuthenticationException(String code) {
        super(code);
    }

    public BadCredentialsAuthenticationException(String code, Throwable throwable) {
        super(code, throwable);
    }
}
