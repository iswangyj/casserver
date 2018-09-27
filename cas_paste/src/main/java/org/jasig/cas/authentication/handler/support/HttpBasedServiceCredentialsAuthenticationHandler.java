package org.jasig.cas.authentication.handler.support;

import org.jasig.cas.authentication.AbstractAuthenticationHandler;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.HttpBasedServiceCredential;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.jasig.cas.util.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.FailedLoginException;
import javax.validation.constraints.NotNull;
import java.security.GeneralSecurityException;

/**
 * @author SxL
 * Created on 9/25/2018 2:34 PM.
 */
public final class HttpBasedServiceCredentialsAuthenticationHandler extends AbstractAuthenticationHandler {
    private static final String PROTOCOL_HTTPS = "https";
    private boolean requireSecure = true;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @NotNull
    private HttpClient httpClient;

    public HttpBasedServiceCredentialsAuthenticationHandler() {
    }

    @Override
    public HandlerResult authenticate(Credential credential) throws GeneralSecurityException {
        HttpBasedServiceCredential httpCredential = (HttpBasedServiceCredential) credential;
        if (this.requireSecure && !httpCredential.getCallbackUrl().getProtocol().equals("https")) {
            this.logger.debug("Authentication failed because url was not secure.");
            throw new FailedLoginException(httpCredential.getCallbackUrl() + " is not an HTTPS endpoint as required.");
        } else {
            this.logger.debug("Attempting to authenticate {}", httpCredential);
            if (!this.httpClient.isValidEndPoint(httpCredential.getCallbackUrl())) {
                throw new FailedLoginException(httpCredential.getCallbackUrl() + " sent an unacceptable response status code");
            } else {
                return new HandlerResult(this, httpCredential, new SimplePrincipal(httpCredential.getId()));
            }
        }
    }

    @Override
    public boolean supports(Credential credential) {
        return credential instanceof HttpBasedServiceCredential;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void setRequireSecure(boolean requireSecure) {
        this.requireSecure = requireSecure;
    }
}

