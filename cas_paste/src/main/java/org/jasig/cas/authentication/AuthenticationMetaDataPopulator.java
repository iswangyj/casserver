package org.jasig.cas.authentication;

/**
 * @author SxL
 * Created on 9/25/2018 1:56 PM.
 */
public interface AuthenticationMetaDataPopulator {
    void populateAttributes(AuthenticationBuilder var1, Credential var2);
}
