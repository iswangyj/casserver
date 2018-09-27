package org.jasig.cas.authentication.principal;

import org.jasig.cas.authentication.AuthenticationBuilder;
import org.jasig.cas.authentication.AuthenticationMetaDataPopulator;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.RememberMeCredential;

/**
 * @author SxL
 * Created on 9/25/2018 2:47 PM.
 */
public final class RememberMeAuthenticationMetaDataPopulator implements AuthenticationMetaDataPopulator {
    public RememberMeAuthenticationMetaDataPopulator() {
    }

    @Override
    public void populateAttributes(AuthenticationBuilder builder, Credential credential) {
        if (credential instanceof RememberMeCredential) {
            RememberMeCredential r = (RememberMeCredential)credential;
            if (r.isRememberMe()) {
                builder.addAttribute("org.jasig.cas.authentication.principal.REMEMBER_ME", true);
            }
        }

    }
}
