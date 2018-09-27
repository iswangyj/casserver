package org.jasig.cas.authentication;

/**
 * @author SxL
 * Created on 9/25/2018 2:03 PM.
 */
public class OneTimePasswordCredential extends AbstractCredential {
    private static final long serialVersionUID = 1892587671827699709L;
    private final String password;
    private String id;

    public OneTimePasswordCredential(String password) {
        if (password == null) {
            throw new IllegalArgumentException("One-time password cannot be null.");
        } else {
            this.password = password;
        }
    }

    public OneTimePasswordCredential(String id, String password) {
        this(password);
        this.id = id;
    }

    public String getPassword() {
        return this.password;
    }

    public String getId() {
        return this.id;
    }
}
