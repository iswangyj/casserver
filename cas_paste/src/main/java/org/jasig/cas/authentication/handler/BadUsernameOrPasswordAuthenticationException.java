package org.jasig.cas.authentication.handler;

/**
 * @author SxL
 * Created on 9/25/2018 2:13 PM.
 */
public class BadUsernameOrPasswordAuthenticationException extends BadCredentialsAuthenticationException {
    public static final BadUsernameOrPasswordAuthenticationException ERROR = new BadUsernameOrPasswordAuthenticationException();
    private static final long serialVersionUID = 3977861752513837361L;
    private static final String CODE = "error.authentication.credentials.bad.usernameorpassword";

    public BadUsernameOrPasswordAuthenticationException() {
        super("error.authentication.credentials.bad.usernameorpassword");
    }

    public BadUsernameOrPasswordAuthenticationException(Throwable throwable) {
        super("error.authentication.credentials.bad.usernameorpassword", throwable);
    }

    public BadUsernameOrPasswordAuthenticationException(String code) {
        super(code);
    }

    public BadUsernameOrPasswordAuthenticationException(String code, Throwable throwable) {
        super(code, throwable);
    }
}
