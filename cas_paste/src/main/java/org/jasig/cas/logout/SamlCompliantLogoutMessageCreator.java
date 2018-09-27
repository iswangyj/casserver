package org.jasig.cas.logout;

import org.jasig.cas.util.DefaultUniqueTicketIdGenerator;
import org.jasig.cas.util.SamlDateUtils;
import org.jasig.cas.util.UniqueTicketIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author SxL
 * Created on 9/25/2018 2:54 PM.
 */
public final class SamlCompliantLogoutMessageCreator implements LogoutMessageCreator {
    private static final Logger LOGGER = LoggerFactory.getLogger(SamlCompliantLogoutMessageCreator.class);
    private static final UniqueTicketIdGenerator GENERATOR = new DefaultUniqueTicketIdGenerator();
    private static final String LOGOUT_REQUEST_TEMPLATE = "<samlp:LogoutRequest xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\" ID=\"%s\" Version=\"2.0\" IssueInstant=\"%s\"><saml:NameID xmlns:saml=\"urn:oasis:names:tc:SAML:2.0:assertion\">@NOT_USED@</saml:NameID><samlp:SessionIndex>%s</samlp:SessionIndex></samlp:LogoutRequest>";

    public SamlCompliantLogoutMessageCreator() {
    }

    @Override
    public String create(LogoutRequest request) {
        String logoutRequest = String.format("<samlp:LogoutRequest xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\" ID=\"%s\" Version=\"2.0\" IssueInstant=\"%s\"><saml:NameID xmlns:saml=\"urn:oasis:names:tc:SAML:2.0:assertion\">@NOT_USED@</saml:NameID><samlp:SessionIndex>%s</samlp:SessionIndex></samlp:LogoutRequest>", GENERATOR.getNewTicketId("LR"), SamlDateUtils.getCurrentDateAndTime(), request.getTicketId());
        LOGGER.debug("Generated logout message: [{}]", logoutRequest);
        return logoutRequest;
    }
}