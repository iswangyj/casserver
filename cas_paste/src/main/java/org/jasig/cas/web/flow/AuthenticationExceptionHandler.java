package org.jasig.cas.web.flow;

import org.jasig.cas.authentication.AccountDisabledException;
import org.jasig.cas.authentication.AuthenticationException;
import org.jasig.cas.authentication.InvalidLoginLocationException;
import org.jasig.cas.authentication.InvalidLoginTimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;

import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.FailedLoginException;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author SxL
 * Created on 9/25/2018 5:37 PM.
 */
public class AuthenticationExceptionHandler {
    private static final String UNKNOWN = "UNKNOWN";
    private static final String DEFAULT_MESSAGE_BUNDLE_PREFIX = "authenticationFailure.";
    private static final List<Class<? extends Exception>> DEFAULT_ERROR_LIST = new ArrayList();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @NotNull
    private List<Class<? extends Exception>> errors;
    private String messageBundlePrefix;

    public AuthenticationExceptionHandler() {
        this.errors = DEFAULT_ERROR_LIST;
        this.messageBundlePrefix = "authenticationFailure.";
    }

    public void setErrors(List<Class<? extends Exception>> errors) {
        this.errors = errors;
    }

    public final List<Class<? extends Exception>> getErrors() {
        return Collections.unmodifiableList(this.errors);
    }

    public void setMessageBundlePrefix(String prefix) {
        this.messageBundlePrefix = prefix;
    }

    public String handle(AuthenticationException e, MessageContext messageContext) {
        if (e != null) {
            Iterator var3 = this.errors.iterator();

            while(var3.hasNext()) {
                Class<? extends Exception> kind = (Class)var3.next();
                Iterator var5 = e.getHandlerErrors().values().iterator();

                while(var5.hasNext()) {
                    Class<? extends Exception> handlerError = (Class)var5.next();
                    if (handlerError != null && handlerError.equals(kind)) {
                        String messageCode = this.messageBundlePrefix + handlerError.getSimpleName();
                        messageContext.addMessage((new MessageBuilder()).error().code(messageCode).build());
                        return handlerError.getSimpleName();
                    }
                }
            }
        }

        String messageCode = this.messageBundlePrefix + "UNKNOWN";
        this.logger.trace("Unable to translate handler errors of the authentication exception {}. Returning {} by default...", e, messageCode);
        messageContext.addMessage((new MessageBuilder()).error().code(messageCode).build());
        return "UNKNOWN";
    }

    static {
        DEFAULT_ERROR_LIST.add(AccountLockedException.class);
        DEFAULT_ERROR_LIST.add(FailedLoginException.class);
        DEFAULT_ERROR_LIST.add(CredentialExpiredException.class);
        DEFAULT_ERROR_LIST.add(AccountNotFoundException.class);
        DEFAULT_ERROR_LIST.add(AccountDisabledException.class);
        DEFAULT_ERROR_LIST.add(InvalidLoginLocationException.class);
        DEFAULT_ERROR_LIST.add(InvalidLoginTimeException.class);
    }
}
