package org.jasig.cas.authentication.handler;

/**
 * @author SxL
 * Created on 9/25/2018 2:13 PM.
 */
public class BlockedCredentialsAuthenticationException extends AuthenticationException {
    public static final BlockedCredentialsAuthenticationException ERROR = new BlockedCredentialsAuthenticationException();
    private static final long serialVersionUID = 3544669598642420017L;
    private static final String CODE = "error.authentication.credentials.blocked";

    public BlockedCredentialsAuthenticationException() {
        super("error.authentication.credentials.blocked");
    }

    public BlockedCredentialsAuthenticationException(Throwable throwable) {
        super("error.authentication.credentials.blocked", throwable);
    }

    public BlockedCredentialsAuthenticationException(String code) {
        super(code);
    }

    public BlockedCredentialsAuthenticationException(String code, Throwable throwable) {
        super(code, throwable);
    }
}
