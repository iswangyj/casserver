package org.jasig.cas.authentication;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author SxL
 * Created on 9/25/2018 2:08 PM.
 */
public class SuccessfulHandlerMetaDataPopulator implements AuthenticationMetaDataPopulator {
    public static final String SUCCESSFUL_AUTHENTICATION_HANDLERS = "successfulAuthenticationHandlers";

    public SuccessfulHandlerMetaDataPopulator() {
    }

    @Override
    public void populateAttributes(AuthenticationBuilder builder, Credential credential) {
        Set<String> successes = builder.getSuccesses().keySet();
        if (successes != null) {
            successes = new HashSet((Collection)successes);
        }

        builder.addAttribute("successfulAuthenticationHandlers", successes);
    }
}
