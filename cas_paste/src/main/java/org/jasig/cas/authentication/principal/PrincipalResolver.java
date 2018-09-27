package org.jasig.cas.authentication.principal;

import org.jasig.cas.authentication.Credential;

/**
 * @author SxL
 * Created on 9/25/2018 2:46 PM.
 */
public interface PrincipalResolver {
    Principal resolve(Credential var1);

    boolean supports(Credential var1);
}
