package org.jasig.cas.authentication.handler.support;

import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.springframework.util.Assert;

import javax.security.auth.callback.*;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.validation.constraints.NotNull;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.util.List;
import java.util.Set;

/**
 * @author SxL
 * Created on 9/25/2018 2:35 PM.
 */
public class JaasAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {
    private static final String DEFAULT_REALM = "CAS";
    @NotNull
    private String realm = "CAS";

    public JaasAuthenticationHandler() {
        Assert.notNull(Configuration.getConfiguration(), "Static Configuration cannot be null. Did you remember to specify \"java.security.auth.login.config\"?");
    }

    @Override
    protected final HandlerResult authenticateUsernamePasswordInternal(UsernamePasswordCredential credential) throws GeneralSecurityException, PreventedException {
        String username = credential.getUsername();
        LoginContext lc = new LoginContext(this.realm, new JaasAuthenticationHandler.UsernamePasswordCallbackHandler(username, credential.getPassword()));

        try {
            this.logger.debug("Attempting authentication for: {}", username);
            lc.login();
        } finally {
            lc.logout();
        }

        SimplePrincipal principal = null;
        Set principals = lc.getSubject().getPrincipals();
        if (principals != null && principals.size() > 0) {
            principal = new SimplePrincipal(((Principal)principals.iterator().next()).getName());
        }

        return this.createHandlerResult(credential, principal, (List)null);
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    protected static final class UsernamePasswordCallbackHandler implements CallbackHandler {
        private final String userName;
        private final String password;

        protected UsernamePasswordCallbackHandler(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        @Override
        public void handle(Callback[] callbacks) throws UnsupportedCallbackException {
            Callback[] var2 = callbacks;
            int var3 = callbacks.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Callback callback = var2[var4];
                if (callback.getClass().equals(NameCallback.class)) {
                    ((NameCallback)callback).setName(this.userName);
                } else {
                    if (!callback.getClass().equals(PasswordCallback.class)) {
                        throw new UnsupportedCallbackException(callback, "Unrecognized Callback");
                    }

                    ((PasswordCallback)callback).setPassword(this.password.toCharArray());
                }
            }

        }
    }
}
