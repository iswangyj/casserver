package org.jasig.cas.validation;

/**
 * @author SxL
 * Created on 9/25/2018 5:18 PM.
 */
public class Cas20ProtocolValidationSpecification extends AbstractCasProtocolValidationSpecification {
    public Cas20ProtocolValidationSpecification() {
    }

    public Cas20ProtocolValidationSpecification(boolean renew) {
        super(renew);
    }

    @Override
    protected boolean isSatisfiedByInternal(Assertion assertion) {
        return true;
    }
}
