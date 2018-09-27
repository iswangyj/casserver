package org.jasig.cas.authentication;

import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * @author SxL
 * Created on 9/25/2018 1:50 PM.
 */
public abstract class AbstractCredential implements Credential, CredentialMetaData, Serializable {
    private static final long serialVersionUID = 8196868021183513898L;

    public AbstractCredential() {
    }

    @Override
    public String toString() {
        return this.getId();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Credential && this.getClass().equals(other.getClass()) ? this.getId().equals(((Credential)other).getId()) : false;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(11, 41);
        builder.append(this.getClass().getName());
        builder.append(this.getId());
        return builder.toHashCode();
    }
}
