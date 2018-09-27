package org.jasig.cas.authentication;

import org.jasig.cas.authentication.principal.Principal;

/**
 * @author SxL
 * Created on 9/25/2018 2:02 PM.
 */
public class MixedPrincipalException extends PrincipalException {
    private static final long serialVersionUID = -9040132618070273997L;
    private final Principal first;
    private final Principal second;

    public MixedPrincipalException(Authentication authentication, Principal a, Principal b) {
        super(a + " != " + b, authentication.getFailures(), authentication.getSuccesses());
        this.first = a;
        this.second = b;
    }

    public Principal getFirst() {
        return this.first;
    }

    public Principal getSecond() {
        return this.second;
    }
}
