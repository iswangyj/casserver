package org.jasig.cas.web.bind;

/**
 * @author SxL
 * Created on 9/25/2018 5:25 PM.
 */

import org.jasig.cas.authentication.Credential;

import javax.servlet.http.HttpServletRequest;

/** @deprecated */
@Deprecated
public interface CredentialsBinder {
    void bind(HttpServletRequest var1, Credential var2);

    boolean supports(Class<?> var1);
}

