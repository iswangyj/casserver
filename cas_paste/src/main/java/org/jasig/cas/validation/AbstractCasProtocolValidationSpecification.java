package org.jasig.cas.validation;

/**
 * @author SxL
 * Created on 9/25/2018 5:17 PM.
 */
public abstract class AbstractCasProtocolValidationSpecification implements ValidationSpecification {
    private static final boolean DEFAULT_RENEW = false;
    private boolean renew;

    public AbstractCasProtocolValidationSpecification() {
        this.renew = false;
    }

    public AbstractCasProtocolValidationSpecification(boolean renew) {
        this.renew = renew;
    }

    public final void setRenew(boolean renew) {
        this.renew = renew;
    }

    public final boolean isRenew() {
        return this.renew;
    }

    public final boolean isSatisfiedBy(Assertion assertion) {
        return this.isSatisfiedByInternal(assertion) && (!this.renew || assertion.isFromNewLogin() && this.renew);
    }

    protected abstract boolean isSatisfiedByInternal(Assertion var1);
}

