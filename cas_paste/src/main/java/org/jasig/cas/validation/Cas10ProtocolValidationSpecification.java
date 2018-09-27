package org.jasig.cas.validation;

/**
 * @author SxL
 * Created on 9/25/2018 5:18 PM.
 */
public final class Cas10ProtocolValidationSpecification extends AbstractCasProtocolValidationSpecification {
    public Cas10ProtocolValidationSpecification() {
    }

    public Cas10ProtocolValidationSpecification(boolean renew) {
        super(renew);
    }

    protected boolean isSatisfiedByInternal(Assertion assertion) {
        return assertion.getChainedAuthentications().size() == 1;
    }
}

