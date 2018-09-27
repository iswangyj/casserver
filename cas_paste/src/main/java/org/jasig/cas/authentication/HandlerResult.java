package org.jasig.cas.authentication;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jasig.cas.Message;
import org.jasig.cas.authentication.principal.Principal;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author SxL
 * Created on 9/25/2018 1:58 PM.
 */
public class HandlerResult implements Serializable {
    private static final long serialVersionUID = -3113998493287982485L;
    private String handlerName;
    private CredentialMetaData credentialMetaData;
    private Principal principal;
    private List<Message> warnings;

    private HandlerResult() {
    }

    public HandlerResult(AuthenticationHandler source, CredentialMetaData metaData) {
        this(source, metaData, (Principal)null, (List)null);
    }

    public HandlerResult(AuthenticationHandler source, CredentialMetaData metaData, Principal p) {
        this(source, metaData, p, (List)null);
    }

    public HandlerResult(AuthenticationHandler source, CredentialMetaData metaData, List<Message> warnings) {
        this(source, metaData, (Principal)null, warnings);
    }

    public HandlerResult(AuthenticationHandler source, CredentialMetaData metaData, Principal p, List<Message> warnings) {
        Assert.notNull(source, "Source cannot be null.");
        Assert.notNull(metaData, "Credential metadata cannot be null.");
        this.handlerName = source.getName();
        if (!StringUtils.hasText(this.handlerName)) {
            this.handlerName = source.getClass().getSimpleName();
        }

        this.credentialMetaData = metaData;
        this.principal = p;
        this.warnings = warnings;
    }

    public String getHandlerName() {
        return this.handlerName;
    }

    public CredentialMetaData getCredentialMetaData() {
        return this.credentialMetaData;
    }

    public Principal getPrincipal() {
        return this.principal;
    }

    public List<Message> getWarnings() {
        return this.warnings == null ? Collections.emptyList() : Collections.unmodifiableList(this.warnings);
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(109, 31);
        builder.append(this.handlerName);
        builder.append(this.credentialMetaData);
        builder.append(this.principal);
        builder.append(this.warnings);
        return builder.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HandlerResult)) {
            return false;
        } else if (obj == this) {
            return true;
        } else {
            HandlerResult other = (HandlerResult)obj;
            EqualsBuilder builder = new EqualsBuilder();
            builder.append(this.handlerName, other.handlerName);
            builder.append(this.credentialMetaData, other.credentialMetaData);
            builder.append(this.principal, other.principal);
            builder.append(this.warnings, other.warnings);
            return builder.isEquals();
        }
    }

    @Override
    public String toString() {
        return this.handlerName + ":" + this.credentialMetaData;
    }
}