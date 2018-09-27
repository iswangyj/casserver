package org.jasig.cas.authentication.handler;

/**
 * @author SxL
 * Created on 9/25/2018 2:14 PM.
 */
public final class NoOpPrincipalNameTransformer implements PrincipalNameTransformer {
    public NoOpPrincipalNameTransformer() {
    }

    @Override
    public String transform(String formUserId) {
        return formUserId;
    }
}
