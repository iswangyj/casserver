package org.jasig.cas.authentication.support;

import org.jasig.cas.Message;

import java.io.Serializable;

/**
 * @author SxL
 * Created on 9/25/2018 2:51 PM.
 */
public class PasswordExpiringWarningMessage extends Message {
    private static final long serialVersionUID = -5892600936676838470L;
    private static final String CODE = "password.expiration.warning";

    public PasswordExpiringWarningMessage(String defaultMsg, long days, String passwordChangeUrl) {
        super("password.expiration.warning", defaultMsg, new Serializable[]{days, passwordChangeUrl});
    }

    public long getDaysToExpiration() {
        return (Long)this.getParams()[0];
    }

    public String getPasswordChangeUrl() {
        return (String)this.getParams()[1];
    }
}
