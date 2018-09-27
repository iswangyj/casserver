package org.jasig.cas.web.flow;

import org.springframework.binding.convert.service.DefaultConversionService;

/**
 * @author SxL
 * Created on 9/25/2018 5:46 PM.
 */
public class LogoutConversionService extends DefaultConversionService {
    public LogoutConversionService() {
        this.addConverter(new CompositeFlowExecutionKeyConverter());
    }
}
