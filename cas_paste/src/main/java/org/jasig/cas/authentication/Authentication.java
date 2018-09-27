package org.jasig.cas.authentication;

import org.jasig.cas.authentication.principal.Principal;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author SxL
 * Created on 9/25/2018 1:54 PM.
 */
public interface Authentication extends Serializable {
    Principal getPrincipal();

    Date getAuthenticatedDate();

    Map<String, Object> getAttributes();

    List<CredentialMetaData> getCredentials();

    Map<String, HandlerResult> getSuccesses();

    Map<String, Class<? extends Exception>> getFailures();
}
