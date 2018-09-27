package org.jasig.cas.authentication;

/**
 * @author SxL
 * Created on 9/25/2018 2:05 PM.
 */
public interface RememberMeCredential extends Credential {
    String AUTHENTICATION_ATTRIBUTE_REMEMBER_ME = "org.jasig.cas.authentication.principal.REMEMBER_ME";
    String REQUEST_PARAMETER_REMEMBER_ME = "rememberMe";

    boolean isRememberMe();

    void setRememberMe(boolean var1);
}
