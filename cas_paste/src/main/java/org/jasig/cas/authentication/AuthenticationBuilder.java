package org.jasig.cas.authentication;

import org.jasig.cas.authentication.principal.Principal;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @author SxL
 * Created on 9/25/2018 1:54 PM.
 */
public class AuthenticationBuilder {
    private Principal principal;
    private List<CredentialMetaData> credentials;
    private Map<String, Object> attributes;
    private Map<String, HandlerResult> successes;
    private Map<String, Class<? extends Exception>> failures;
    private Date authenticationDate;

    public AuthenticationBuilder() {
        this.credentials = new ArrayList();
        this.attributes = new LinkedHashMap();
        this.successes = new LinkedHashMap();
        this.failures = new LinkedHashMap();
        this.authenticationDate = new Date();
    }

    public AuthenticationBuilder(Principal p) {
        this();
        this.principal = p;
    }

    public Date getAuthenticationDate() {
        return this.authenticationDate;
    }

    public AuthenticationBuilder setAuthenticationDate(Date d) {
        this.authenticationDate = d;
        return this;
    }

    public Principal getPrincipal() {
        return this.principal;
    }

    public AuthenticationBuilder setPrincipal(Principal p) {
        this.principal = p;
        return this;
    }

    public List<CredentialMetaData> getCredentials() {
        return this.credentials;
    }

    public AuthenticationBuilder setCredentials(List<CredentialMetaData> credentials) {
        Assert.notNull(credentials, "Credential cannot be null");
        this.credentials.clear();
        Iterator var2 = credentials.iterator();

        while(var2.hasNext()) {
            CredentialMetaData c = (CredentialMetaData)var2.next();
            this.credentials.add(c);
        }

        return this;
    }

    public AuthenticationBuilder addCredential(CredentialMetaData credential) {
        this.credentials.add(credential);
        return this;
    }

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    public AuthenticationBuilder setAttributes(Map<String, Object> attributes) {
        Assert.notNull(attributes, "Attributes cannot be null");
        this.attributes.clear();
        Iterator var2 = attributes.keySet().iterator();

        while(var2.hasNext()) {
            String name = (String)var2.next();
            this.attributes.put(name, attributes.get(name));
        }

        return this;
    }

    public AuthenticationBuilder addAttribute(String key, Object value) {
        this.attributes.put(key, value);
        return this;
    }

    public Map<String, HandlerResult> getSuccesses() {
        return this.successes;
    }

    public AuthenticationBuilder setSuccesses(Map<String, HandlerResult> successes) {
        Assert.notNull(successes, "Successes cannot be null");
        this.successes.clear();
        Iterator var2 = successes.keySet().iterator();

        while(var2.hasNext()) {
            String handler = (String)var2.next();
            this.successes.put(handler, successes.get(handler));
        }

        return this;
    }

    public AuthenticationBuilder addSuccess(String key, HandlerResult value) {
        this.successes.put(key, value);
        return this;
    }

    public Map<String, Class<? extends Exception>> getFailures() {
        return this.failures;
    }

    public AuthenticationBuilder setFailures(Map<String, Class<? extends Exception>> failures) {
        Assert.notNull(failures, "Failures cannot be null");
        this.failures.clear();
        Iterator var2 = failures.keySet().iterator();

        while(var2.hasNext()) {
            String handler = (String)var2.next();
            this.failures.put(handler, failures.get(handler));
        }

        return this;
    }

    public AuthenticationBuilder addFailure(String key, Class<? extends Exception> value) {
        this.failures.put(key, value);
        return this;
    }

    public Authentication build() {
        return new ImmutableAuthentication(this.authenticationDate, this.credentials, this.principal, this.attributes, this.successes, this.failures);
    }

    public static AuthenticationBuilder newInstance(Authentication source) {
        AuthenticationBuilder builder = new AuthenticationBuilder(source.getPrincipal());
        builder.setAuthenticationDate(source.getAuthenticatedDate());
        builder.setCredentials(source.getCredentials());
        builder.setSuccesses(source.getSuccesses());
        builder.setFailures(source.getFailures());
        builder.setAttributes(source.getAttributes());
        return builder;
    }
}
