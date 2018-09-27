package org.jasig.cas.web.support;

import org.jasig.cas.authentication.principal.WebApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @author SxL
 * Created on 9/25/2018 5:53 PM.
 */
public abstract class AbstractArgumentExtractor implements ArgumentExtractor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public AbstractArgumentExtractor() {
    }

    public final WebApplicationService extractService(HttpServletRequest request) {
        WebApplicationService service = this.extractServiceInternal(request);
        if (service == null) {
            this.logger.debug("Extractor did not generate service.");
        } else {
            this.logger.debug("Extractor generated service for: {}", service.getId());
        }

        return service;
    }

    protected abstract WebApplicationService extractServiceInternal(HttpServletRequest var1);
}
