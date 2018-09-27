package org.jasig.cas.authentication.handler.support;

import org.jasig.cas.Message;
import org.jasig.cas.authentication.*;
import org.jasig.cas.authentication.handler.NoOpPrincipalNameTransformer;
import org.jasig.cas.authentication.handler.PasswordEncoder;
import org.jasig.cas.authentication.handler.PlainTextPasswordEncoder;
import org.jasig.cas.authentication.handler.PrincipalNameTransformer;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.support.PasswordPolicyConfiguration;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.constraints.NotNull;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * @author SxL
 * Created on 9/25/2018 2:17 PM.
 */
public abstract class AbstractUsernamePasswordAuthenticationHandler extends AbstractPreAndPostProcessingAuthenticationHandler {
    @NotNull
    private PasswordEncoder passwordEncoder = new PlainTextPasswordEncoder();
    @NotNull
    private PrincipalNameTransformer principalNameTransformer = new NoOpPrincipalNameTransformer();
    private PasswordPolicyConfiguration passwordPolicyConfiguration;

    public AbstractUsernamePasswordAuthenticationHandler() {
    }

    @Override
    protected final HandlerResult doAuthentication(Credential credential) throws GeneralSecurityException, PreventedException {
        UsernamePasswordCredential userPass = (UsernamePasswordCredential)credential;
        if (userPass.getUsername() == null) {
            throw new AccountNotFoundException("Username is null.");
        } else {
            String transformedUsername = this.principalNameTransformer.transform(userPass.getUsername());
            if (transformedUsername == null) {
                throw new AccountNotFoundException("Transformed username is null.");
            } else {
                userPass.setUsername(transformedUsername);
                return this.authenticateUsernamePasswordInternal(userPass);
            }
        }
    }

    protected abstract HandlerResult authenticateUsernamePasswordInternal(UsernamePasswordCredential var1) throws GeneralSecurityException, PreventedException;

    protected final PasswordEncoder getPasswordEncoder() {
        return this.passwordEncoder;
    }

    protected final PrincipalNameTransformer getPrincipalNameTransformer() {
        return this.principalNameTransformer;
    }

    protected final PasswordPolicyConfiguration getPasswordPolicyConfiguration() {
        return this.passwordPolicyConfiguration;
    }

    protected final HandlerResult createHandlerResult(Credential credential, Principal principal, List<Message> warnings) {
        return new HandlerResult(this, new BasicCredentialMetaData(credential), principal, warnings);
    }

    public final void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public final void setPrincipalNameTransformer(PrincipalNameTransformer principalNameTransformer) {
        this.principalNameTransformer = principalNameTransformer;
    }

    public final void setPasswordPolicyConfiguration(PasswordPolicyConfiguration passwordPolicyConfiguration) {
        this.passwordPolicyConfiguration = passwordPolicyConfiguration;
    }

    @Override
    public boolean supports(Credential credential) {
        return credential instanceof UsernamePasswordCredential;
    }
}