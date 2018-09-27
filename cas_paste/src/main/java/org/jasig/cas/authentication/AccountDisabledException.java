package org.jasig.cas.authentication;

import javax.security.auth.login.AccountException;

/**
 * @author SxL
 * Created on 9/25/2018 1:52 PM.
 */
public class AccountDisabledException extends AccountException {
    private static final long serialVersionUID = 7487835035108753209L;

    public AccountDisabledException() {
    }

    public AccountDisabledException(String msg) {
        super(msg);
    }
}
