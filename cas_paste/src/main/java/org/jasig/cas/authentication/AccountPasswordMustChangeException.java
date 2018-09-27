package org.jasig.cas.authentication;

import javax.security.auth.login.CredentialExpiredException;

/**
 * @author SxL
 * Created on 9/25/2018 1:52 PM.
 */
public class AccountPasswordMustChangeException extends CredentialExpiredException {
    private static final long serialVersionUID = 7487835035108753209L;

    public AccountPasswordMustChangeException() {
    }

    public AccountPasswordMustChangeException(String msg) {
        super(msg);
    }
}
