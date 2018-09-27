package org.jasig.cas.authentication;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * @author SxL
 * Created on 9/25/2018 1:56 PM.
 */
public class BasicCredentialMetaData implements CredentialMetaData, Serializable {
    private static final long serialVersionUID = 4929579849241505377L;
    private final String id;
    private Class<? extends Credential> credentialClass;

    private BasicCredentialMetaData() {
        this.id = null;
    }

    public BasicCredentialMetaData(Credential credential) {
        this.id = credential.getId();
        this.credentialClass = credential.getClass();
    }

    public String getId() {
        return this.id;
    }

    public Class<? extends Credential> getCredentialClass() {
        return this.credentialClass;
    }

    public int hashCode() {
        return (new HashCodeBuilder(19, 21)).append(this.id).append(this.credentialClass).toHashCode();
    }

    public boolean equals(Object other) {
        if (!(other instanceof BasicCredentialMetaData)) {
            return false;
        } else {
            BasicCredentialMetaData md = (BasicCredentialMetaData)other;
            EqualsBuilder builder = new EqualsBuilder();
            builder.append(this.id, md.id);
            builder.append(this.credentialClass, md.credentialClass);
            return builder.isEquals();
        }
    }
}