package org.jasig.cas.authentication;

/**
 * @author SxL
 * Created on 9/25/2018 1:57 PM.
 */
public interface ContextualAuthenticationPolicyFactory<T> {
    ContextualAuthenticationPolicy<T> createPolicy(T var1);
}
