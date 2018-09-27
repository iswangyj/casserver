package org.jasig.cas.authentication;

import java.util.Collections;
import java.util.Map;

/**
 * @author SxL
 * Created on 9/25/2018 1:55 PM.
 */
public class AuthenticationException extends Exception {
    private static final long serialVersionUID = -6032827784134751797L;
    private final Map<String, Class<? extends Exception>> handlerErrors;
    private final Map<String, HandlerResult> handlerSuccesses;

    public AuthenticationException() {
        this("No supported authentication handlers found for given credentials.", Collections.emptyMap(), Collections.emptyMap());
    }

    public AuthenticationException(Map<String, Class<? extends Exception>> handlerErrors) {
        this(handlerErrors, Collections.emptyMap());
    }

    public AuthenticationException(Map<String, Class<? extends Exception>> handlerErrors, Map<String, HandlerResult> handlerSuccesses) {
        this(String.format("%s errors, %s successes", handlerErrors.size(), handlerSuccesses.size()), handlerErrors, handlerSuccesses);
    }

    public AuthenticationException(String message, Map<String, Class<? extends Exception>> handlerErrors, Map<String, HandlerResult> handlerSuccesses) {
        super(message);
        this.handlerErrors = Collections.unmodifiableMap(handlerErrors);
        this.handlerSuccesses = Collections.unmodifiableMap(handlerSuccesses);
    }

    public Map<String, Class<? extends Exception>> getHandlerErrors() {
        return this.handlerErrors;
    }

    public Map<String, HandlerResult> getHandlerSuccesses() {
        return this.handlerSuccesses;
    }
}
