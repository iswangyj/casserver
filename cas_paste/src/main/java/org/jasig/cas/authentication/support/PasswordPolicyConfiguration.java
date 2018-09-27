package org.jasig.cas.authentication.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author SxL
 * Created on 9/25/2018 2:51 PM.
 */
public class PasswordPolicyConfiguration {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private boolean alwaysDisplayPasswordExpirationWarning = false;
    private int passwordWarningNumberOfDays = 30;
    private String passwordPolicyUrl;

    public PasswordPolicyConfiguration() {
    }

    public boolean isAlwaysDisplayPasswordExpirationWarning() {
        return this.alwaysDisplayPasswordExpirationWarning;
    }

    public void setAlwaysDisplayPasswordExpirationWarning(boolean alwaysDisplayPasswordExpirationWarning) {
        this.alwaysDisplayPasswordExpirationWarning = alwaysDisplayPasswordExpirationWarning;
    }

    public String getPasswordPolicyUrl() {
        return this.passwordPolicyUrl;
    }

    public void setPasswordPolicyUrl(String passwordPolicyUrl) {
        this.passwordPolicyUrl = passwordPolicyUrl;
    }

    public int getPasswordWarningNumberOfDays() {
        return this.passwordWarningNumberOfDays;
    }

    public void setPasswordWarningNumberOfDays(int days) {
        this.passwordWarningNumberOfDays = days;
    }
}
