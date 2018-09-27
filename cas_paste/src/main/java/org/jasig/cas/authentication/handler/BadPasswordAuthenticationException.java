package org.jasig.cas.authentication.handler;

/**
 * @author SxL
 * Created on 9/25/2018 2:12 PM.
 */
public class BadPasswordAuthenticationException extends BadUsernameOrPasswordAuthenticationException {
    public static final BadPasswordAuthenticationException ERROR = new BadPasswordAuthenticationException();
    private static final long serialVersionUID = 3977861752513837361L;
    private static final String CODE = "error.authentication.credentials.bad.usernameorpassword.password";

    public BadPasswordAuthenticationException() {
        super("error.authentication.credentials.bad.usernameorpassword.password");
    }

    public BadPasswordAuthenticationException(Throwable throwable) {
        super("error.authentication.credentials.bad.usernameorpassword.password", throwable);
    }

    public BadPasswordAuthenticationException(String code) {
        super(code);
    }

    public BadPasswordAuthenticationException(String code, Throwable throwable) {
        super(code, throwable);
    }
}
