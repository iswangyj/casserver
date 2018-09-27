package org.jasig.cas.authentication;

/**
 * @author SxL
 * Created on 9/25/2018 2:08 PM.
 */
public class UnresolvedPrincipalException extends PrincipalException {
    private static final long serialVersionUID = 380456166081802820L;
    private static final String UNRESOLVED_PRINCIPAL = "No resolver produced a principal.";

    public UnresolvedPrincipalException(Authentication authentication) {
        super("No resolver produced a principal.", authentication.getFailures(), authentication.getSuccesses());
    }

    public UnresolvedPrincipalException(Authentication authentication, Exception cause) {
        super(cause.getMessage(), authentication.getFailures(), authentication.getSuccesses());
    }
}
