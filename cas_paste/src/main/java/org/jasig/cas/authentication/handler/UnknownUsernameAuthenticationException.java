package org.jasig.cas.authentication.handler;

/**
 * @author SxL
 * Created on 9/25/2018 2:15 PM.
 */
public class UnknownUsernameAuthenticationException extends BadUsernameOrPasswordAuthenticationException {
    public static final UnknownUsernameAuthenticationException ERROR = new UnknownUsernameAuthenticationException();
    private static final long serialVersionUID = 3977861752513837361L;
    private static final String CODE = "error.authentication.credentials.bad.usernameorpassword.username";

    public UnknownUsernameAuthenticationException() {
        super("error.authentication.credentials.bad.usernameorpassword.username");
    }

    public UnknownUsernameAuthenticationException(Throwable throwable) {
        super("error.authentication.credentials.bad.usernameorpassword.username", throwable);
    }

    public UnknownUsernameAuthenticationException(String code) {
        super(code);
    }

    public UnknownUsernameAuthenticationException(String code, Throwable throwable) {
        super(code, throwable);
    }
}
