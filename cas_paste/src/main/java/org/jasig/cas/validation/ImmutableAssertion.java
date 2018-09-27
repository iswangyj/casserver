package org.jasig.cas.validation;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.principal.Service;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author SxL
 * Created on 9/25/2018 5:18 PM.
 */
public final class ImmutableAssertion implements Assertion, Serializable {
    private static final long serialVersionUID = -3348826049921010423L;
    private final Authentication primaryAuthentication;
    private final List<Authentication> chainedAuthentications;
    private final boolean fromNewLogin;
    private final Service service;

    public ImmutableAssertion(Authentication primary, List<Authentication> chained, Service service, boolean fromNewLogin) {
        Assert.notNull(primary, "primary authentication cannot be null");
        Assert.notNull(chained, "chained authentications cannot be null");
        Assert.notNull(service, "service cannot be null");
        Assert.notEmpty(chained, "chained authentications cannot be empty");
        this.primaryAuthentication = primary;
        this.chainedAuthentications = chained;
        this.service = service;
        this.fromNewLogin = fromNewLogin;
    }

    @Override
    public Authentication getPrimaryAuthentication() {
        return this.primaryAuthentication;
    }

    @Override
    public List<Authentication> getChainedAuthentications() {
        return Collections.unmodifiableList(this.chainedAuthentications);
    }

    @Override
    public boolean isFromNewLogin() {
        return this.fromNewLogin;
    }

    @Override
    public Service getService() {
        return this.service;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Assertion)) {
            return false;
        } else {
            Assertion a = (Assertion)o;
            return this.primaryAuthentication.equals(a.getPrimaryAuthentication()) && this.chainedAuthentications.equals(a.getChainedAuthentications()) && this.service.equals(a.getService()) && this.fromNewLogin == a.isFromNewLogin();
        }
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(15, 11);
        builder.append(this.primaryAuthentication);
        builder.append(this.chainedAuthentications);
        builder.append(this.service);
        builder.append(this.fromNewLogin);
        return builder.toHashCode();
    }

    @Override
    public String toString() {
        return this.primaryAuthentication + ":" + this.service;
    }
}
