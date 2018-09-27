package org.jasig.cas.services.support;

import org.jasig.cas.services.RegisteredService;
import org.jasig.cas.services.RegisteredServiceAttributeFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author SxL
 * Created on 9/25/2018 3:23 PM.
 */
public final class RegisteredServiceDefaultAttributeFilter implements RegisteredServiceAttributeFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public RegisteredServiceDefaultAttributeFilter() {
    }

    @Override
    public Map<String, Object> filter(String principalId, Map<String, Object> givenAttributes, RegisteredService registeredService) {
        Map<String, Object> attributes = new HashMap();
        if (registeredService.isIgnoreAttributes()) {
            this.logger.debug("Service [{}] is set to ignore attribute release policy. Releasing all attributes.", registeredService.getName());
            attributes.putAll(givenAttributes);
        } else {
            Iterator var5 = registeredService.getAllowedAttributes().iterator();

            while(var5.hasNext()) {
                String attribute = (String)var5.next();
                Object value = givenAttributes.get(attribute);
                if (value != null) {
                    this.logger.debug("Found attribute [{}] in the list of allowed attributes for service [{}]", attribute, registeredService.getName());
                    attributes.put(attribute, value);
                }
            }
        }

        return Collections.unmodifiableMap(attributes);
    }
}
