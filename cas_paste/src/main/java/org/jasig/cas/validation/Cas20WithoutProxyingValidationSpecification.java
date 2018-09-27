package org.jasig.cas.validation;

/**
 * @author SxL
 * Created on 9/25/2018 5:18 PM.
 */
public class Cas20WithoutProxyingValidationSpecification extends AbstractCasProtocolValidationSpecification {
    public Cas20WithoutProxyingValidationSpecification() {
    }

    public Cas20WithoutProxyingValidationSpecification(boolean renew) {
        super(renew);
    }

    @Override
    protected boolean isSatisfiedByInternal(Assertion assertion) {
        return assertion.getChainedAuthentications().size() == 1;
    }
}

