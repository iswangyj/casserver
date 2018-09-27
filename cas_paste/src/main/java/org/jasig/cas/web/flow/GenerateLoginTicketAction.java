package org.jasig.cas.web.flow;

import org.jasig.cas.util.UniqueTicketIdGenerator;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.webflow.execution.RequestContext;

import javax.validation.constraints.NotNull;

/**
 * @author SxL
 * Created on 9/25/2018 5:43 PM.
 */
public class GenerateLoginTicketAction {
    private static final String PREFIX = "LT";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @NotNull
    private UniqueTicketIdGenerator ticketIdGenerator;

    public GenerateLoginTicketAction() {
    }

    public final String generate(RequestContext context) {
        String loginTicket = this.ticketIdGenerator.getNewTicketId("LT");
        this.logger.debug("Generated login ticket {}", loginTicket);
        WebUtils.putLoginTicket(context, loginTicket);
        return "generated";
    }

    public void setTicketIdGenerator(UniqueTicketIdGenerator generator) {
        this.ticketIdGenerator = generator;
    }
}
