package org.jasig.cas.authentication.principal;

/**
 * @author SxL
 * Created on 9/25/2018 2:48 PM.
 */
public interface Service extends Principal {
    void setPrincipal(Principal var1);

    boolean matches(Service var1);
}