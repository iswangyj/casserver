package org.jasig.cas.authentication.handler;

/**
 * @author SxL
 * Created on 9/25/2018 2:15 PM.
 */
public final class UnsupportedCredentialsException extends AuthenticationException {
    public static final UnsupportedCredentialsException ERROR = new UnsupportedCredentialsException();
    private static final long serialVersionUID = 3977861752513837361L;
    private static final String CODE = "error.authentication.credentials.unsupported";

    public UnsupportedCredentialsException() {
        super("error.authentication.credentials.unsupported");
    }

    public UnsupportedCredentialsException(Throwable throwable) {
        super("error.authentication.credentials.unsupported", throwable);
    }
}
