package org.jasig.cas.authentication;

/**
 * @author SxL
 * Created on 9/25/2018 1:55 PM.
 */
public interface AuthenticationManager {
    String AUTHENTICATION_METHOD_ATTRIBUTE = "authenticationMethod";

    Authentication authenticate(Credential... var1) throws AuthenticationException;
}
