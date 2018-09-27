package org.jasig.cas.ticket;

import org.jasig.cas.authentication.ContextualAuthenticationPolicy;
import org.springframework.util.Assert;

/**
 * @author SxL
 * Created on 9/25/2018 3:38 PM.
 */
public class UnsatisfiedAuthenticationPolicyException extends TicketException {
    private static final long serialVersionUID = -827432780367197133L;
    private static final String CODE = "UNSATISFIED_AUTHN_POLICY";
    private final ContextualAuthenticationPolicy<?> policy;

    public UnsatisfiedAuthenticationPolicyException(ContextualAuthenticationPolicy<?> policy) {
        super("UNSATISFIED_AUTHN_POLICY");
        Assert.notNull(policy, "ContextualAuthenticationPolicy cannot be null");
        this.policy = policy;
    }

    public ContextualAuthenticationPolicy<?> getPolicy() {
        return this.policy;
    }
}

