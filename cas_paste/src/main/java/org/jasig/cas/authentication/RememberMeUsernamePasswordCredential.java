package org.jasig.cas.authentication;

import java.io.Serializable;

/**
 * @author SxL
 * Created on 9/25/2018 2:05 PM.
 */
public class RememberMeUsernamePasswordCredential extends UsernamePasswordCredential implements RememberMeCredential, Serializable {
    private static final long serialVersionUID = -6710007659431302397L;
    private boolean rememberMe;

    public RememberMeUsernamePasswordCredential() {
    }

    public final boolean isRememberMe() {
        return this.rememberMe;
    }

    @Override
    public int hashCode() {
        boolean prime = true;
        int result = super.hashCode();
        result = 31 * result + (this.rememberMe ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!super.equals(obj)) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            RememberMeUsernamePasswordCredential other = (RememberMeUsernamePasswordCredential)obj;
            return this.rememberMe == other.rememberMe;
        }
    }

    public final void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
