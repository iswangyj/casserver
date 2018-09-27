package org.jasig.cas.authentication;

/**
 * @author SxL
 * Created on 9/25/2018 1:50 PM.
 */
public abstract class AbstractAuthenticationHandler implements AuthenticationHandler {
    private String name;

    public AbstractAuthenticationHandler() {
    }

    @Override
    public String getName() {
        return this.name != null ? this.name : this.getClass().getSimpleName();
    }

    public void setName(String name) {
        this.name = name;
    }
}
