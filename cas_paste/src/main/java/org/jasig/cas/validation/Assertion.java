package org.jasig.cas.validation;

import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.principal.Service;

import java.util.List;

/**
 * @author SxL
 * Created on 9/25/2018 5:17 PM.
 */
public interface Assertion {
    Authentication getPrimaryAuthentication();

    List<Authentication> getChainedAuthentications();

    boolean isFromNewLogin();

    Service getService();
}
