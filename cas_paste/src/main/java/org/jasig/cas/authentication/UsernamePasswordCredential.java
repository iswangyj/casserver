package org.jasig.cas.authentication;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author SxL
 * Created on 9/25/2018 2:09 PM.
 */
public class UsernamePasswordCredential implements Credential, Serializable {
    private static final long serialVersionUID = -700605081472810939L;
    private static final String PASSWORD_SUFFIX = "+password";
    @NotNull
    @Size(
            min = 1,
            message = "required.username"
    )
    private String username;
    @NotNull
    @Size(
            min = 1,
            message = "required.password"
    )
    private String password;
    private String bSpecial = null;
    private String strCA = null;
    private String service = null;

    public UsernamePasswordCredential() {
    }

    public UsernamePasswordCredential(String userName, String password) {
        this.username = userName;
        this.password = password;
    }

    public final String getPassword() {
        return this.password;
    }

    public final void setPassword(String password) {
        this.password = password;
    }

    public final String getUsername() {
        return this.username;
    }

    public final void setUsername(String userName) {
        this.username = userName;
    }

    public String getbSpecial() {
        return this.bSpecial;
    }

    public void setbSpecial(String bSpecial) {
        this.bSpecial = bSpecial;
    }

    public String getStrCA() {
        return this.strCA;
    }

    public void setStrCA(String strCA) {
        this.strCA = strCA;
    }

    public String getService() {
        return this.service;
    }

    public void setService(String service) {
        this.service = service;
    }

    @Override
    public String getId() {
        return this.username;
    }

    @Override
    public String toString() {
        return this.username + "+password";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            UsernamePasswordCredential that = (UsernamePasswordCredential)o;
            if (this.password != null) {
                if (!this.password.equals(that.password)) {
                    return false;
                }
            } else if (that.password != null) {
                return false;
            }

            if (this.username != null) {
                if (this.username.equals(that.username)) {
                    return true;
                }
            } else if (that.username == null) {
                return true;
            }

            return false;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = this.username != null ? this.username.hashCode() : 0;
        result = 31 * result + (this.password != null ? this.password.hashCode() : 0);
        return result;
    }
}
