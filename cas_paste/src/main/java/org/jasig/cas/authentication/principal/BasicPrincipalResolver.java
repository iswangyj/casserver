package org.jasig.cas.authentication.principal;

import org.jasig.cas.authentication.Credential;

/**
 * @author SxL
 * Created on 9/25/2018 2:39 PM.
 */
public class BasicPrincipalResolver implements PrincipalResolver {
    public BasicPrincipalResolver() {
    }

    @Override
    public Principal resolve(Credential credential) {
        return new SimplePrincipal(credential.getId());
    }

    @Override
    public boolean supports(Credential credential) {
        return credential.getId() != null;
    }
}
