package org.jasig.cas.ticket.proxy.support;

import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.HttpBasedServiceCredential;
import org.jasig.cas.ticket.proxy.ProxyHandler;
import org.jasig.cas.util.DefaultUniqueTicketIdGenerator;
import org.jasig.cas.util.HttpClient;
import org.jasig.cas.util.UniqueTicketIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

/**
 * @author SxL
 * Created on 9/25/2018 3:40 PM.
 */
public final class Cas20ProxyHandler implements ProxyHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String PGTIOU_PREFIX = "PGTIOU";
    private static final String PARAMETER_PROXY_GRANTING_TICKET_IOU = "pgtIou";
    private static final String PARAMETER_PROXY_GRANTING_TICKET_ID = "pgtId";
    @NotNull
    private UniqueTicketIdGenerator uniqueTicketIdGenerator = new DefaultUniqueTicketIdGenerator();
    @NotNull
    private HttpClient httpClient;

    public Cas20ProxyHandler() {
    }

    @Override
    public String handle(Credential credential, String proxyGrantingTicketId) {
        HttpBasedServiceCredential serviceCredentials = (HttpBasedServiceCredential)credential;
        String proxyIou = this.uniqueTicketIdGenerator.getNewTicketId("PGTIOU");
        String serviceCredentialsAsString = serviceCredentials.getCallbackUrl().toExternalForm();
        int bufferLength = serviceCredentialsAsString.length() + proxyIou.length() + proxyGrantingTicketId.length() + 15;
        StringBuilder stringBuffer = new StringBuilder(bufferLength);
        stringBuffer.append(serviceCredentialsAsString);
        if (serviceCredentials.getCallbackUrl().getQuery() != null) {
            stringBuffer.append("&");
        } else {
            stringBuffer.append("?");
        }

        stringBuffer.append("pgtIou");
        stringBuffer.append("=");
        stringBuffer.append(proxyIou);
        stringBuffer.append("&");
        stringBuffer.append("pgtId");
        stringBuffer.append("=");
        stringBuffer.append(proxyGrantingTicketId);
        if (this.httpClient.isValidEndPoint(stringBuffer.toString())) {
            this.logger.debug("Sent ProxyIou of {} for service: {}", proxyIou, serviceCredentials.toString());
            return proxyIou;
        } else {
            this.logger.debug("Failed to send ProxyIou of {} for service: {}", proxyIou, serviceCredentials.toString());
            return null;
        }
    }

    public void setUniqueTicketIdGenerator(UniqueTicketIdGenerator uniqueTicketIdGenerator) {
        this.uniqueTicketIdGenerator = uniqueTicketIdGenerator;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public boolean canHandle(Credential credential) {
        return true;
    }
}
