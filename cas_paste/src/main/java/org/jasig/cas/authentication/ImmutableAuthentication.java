package org.jasig.cas.authentication;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jasig.cas.authentication.principal.Principal;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author SxL
 * Created on 9/25/2018 2:00 PM.
 */
public class ImmutableAuthentication implements Authentication, Serializable {
    private static final long serialVersionUID = 3206127526058061391L;
    private final long authenticatedDate;
    private final List<CredentialMetaData> credentials;
    private final Principal principal;
    private final Map<String, Object> attributes;
    private final Map<String, HandlerResult> successes;
    private final Map<String, Class<? extends Exception>> failures;

    private ImmutableAuthentication() {
        this.authenticatedDate = 0L;
        this.credentials = null;
        this.principal = null;
        this.attributes = null;
        this.successes = null;
        this.failures = null;
    }

    public ImmutableAuthentication(Date date, List<CredentialMetaData> credentials, Principal principal, Map<String, Object> attributes, Map<String, HandlerResult> successes, Map<String, Class<? extends Exception>> failures) {
        Assert.notNull(date, "Date cannot be null");
        Assert.notNull(credentials, "Credential cannot be null");
        Assert.notNull(principal, "Principal cannot be null");
        Assert.notNull(successes, "Successes cannot be null");
        Assert.notEmpty(credentials, "Credential cannot be empty");
        Assert.notEmpty(successes, "Successes cannot be empty");
        this.authenticatedDate = date.getTime();
        this.credentials = credentials;
        this.principal = principal;
        this.attributes = attributes.isEmpty() ? null : attributes;
        this.successes = successes;
        this.failures = failures.isEmpty() ? null : failures;
    }

    @Override
    public Principal getPrincipal() {
        return this.principal;
    }

    @Override
    public Date getAuthenticatedDate() {
        return new ImmutableAuthentication.ImmutableDate(this.authenticatedDate);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return wrap(this.attributes);
    }

    @Override
    public List<CredentialMetaData> getCredentials() {
        return Collections.unmodifiableList(this.credentials);
    }

    @Override
    public Map<String, HandlerResult> getSuccesses() {
        return Collections.unmodifiableMap(this.successes);
    }

    @Override
    public Map<String, Class<? extends Exception>> getFailures() {
        return wrap(this.failures);
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(97, 31);
        builder.append(this.principal);
        builder.append(this.authenticatedDate);
        builder.append(this.attributes);
        builder.append(this.credentials);
        builder.append(this.successes);
        builder.append(this.failures);
        return builder.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Authentication)) {
            return false;
        } else if (obj == this) {
            return true;
        } else {
            Authentication other = (Authentication)obj;
            EqualsBuilder builder = new EqualsBuilder();
            builder.append(this.principal, other.getPrincipal());
            builder.append(this.credentials, other.getCredentials());
            builder.append(this.successes, other.getSuccesses());
            builder.append(this.authenticatedDate, other.getAuthenticatedDate().getTime());
            builder.append(wrap(this.attributes), other.getAttributes());
            builder.append(wrap(this.failures), other.getFailures());
            return builder.isEquals();
        }
    }

    private static <K, V> Map<K, V> wrap(Map<K, V> source) {
        return source != null ? Collections.unmodifiableMap(source) : Collections.emptyMap();
    }

    private static final class ImmutableDate extends Date {
        private ImmutableDate() {
        }

        public ImmutableDate(long instant) {
            super(instant);
        }

        public void setYear(int year) {
            throw new UnsupportedOperationException();
        }

        public void setDate(int date) {
            throw new UnsupportedOperationException();
        }

        public void setHours(int hours) {
            throw new UnsupportedOperationException();
        }

        public void setMinutes(int minutes) {
            throw new UnsupportedOperationException();
        }

        public void setSeconds(int seconds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setTime(long time) {
            throw new UnsupportedOperationException();
        }
    }
}
