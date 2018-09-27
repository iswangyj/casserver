package org.jasig.cas.web.support;

import org.jasig.cas.authentication.principal.SimpleWebApplicationServiceImpl;
import org.jasig.cas.authentication.principal.WebApplicationService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author SxL
 * Created on 9/25/2018 5:54 PM.
 */
public final class CasArgumentExtractor extends AbstractArgumentExtractor {
    public CasArgumentExtractor() {
    }

    @Override
    public WebApplicationService extractServiceInternal(HttpServletRequest request) {
        return SimpleWebApplicationServiceImpl.createServiceFrom(request);
    }
}
