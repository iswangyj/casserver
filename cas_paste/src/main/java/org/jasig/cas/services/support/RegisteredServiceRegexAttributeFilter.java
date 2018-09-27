package org.jasig.cas.services.support;

import org.jasig.cas.services.RegisteredService;
import org.jasig.cas.services.RegisteredServiceAttributeFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author SxL
 * Created on 9/25/2018 3:23 PM.
 */
public class RegisteredServiceRegexAttributeFilter implements RegisteredServiceAttributeFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @NotNull
    private Pattern pattern;

    public RegisteredServiceRegexAttributeFilter(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public Map<String, Object> filter(String principalId, Map<String, Object> givenAttributes, RegisteredService registeredService) {
        Map<String, Object> attributesToRelease = new HashMap();
        Iterator var5 = givenAttributes.keySet().iterator();

        while(var5.hasNext()) {
            String attributeName = (String)var5.next();
            Object attributeValue = givenAttributes.get(attributeName);
            this.logger.debug("Received attribute [{}] with value [{}]", attributeName, attributeValue);
            if (attributeValue != null) {
                String[] filteredAttributes;
                if (attributeValue instanceof Collection) {
                    filteredAttributes = this.filterArrayAttributes((String[])((Collection)attributeValue).toArray(new String[0]), attributeName);
                    if (filteredAttributes.length > 0) {
                        attributesToRelease.put(attributeName, filteredAttributes);
                    }
                } else if (attributeValue.getClass().isArray()) {
                    filteredAttributes = this.filterArrayAttributes((String[])((String[])attributeValue), attributeName);
                    if (filteredAttributes.length > 0) {
                        attributesToRelease.put(attributeName, filteredAttributes);
                    }
                } else if (attributeValue instanceof Map) {
                    Map<String, String> filteredAttribute = this.filterMapAttributes((Map)attributeValue);
                    if (filteredAttribute.size() > 0) {
                        attributesToRelease.put(attributeName, filteredAttribute);
                    }
                } else if (this.patternMatchesAttributeValue(attributeValue.toString())) {
                    this.logReleasedAttributeEntry(attributeName, attributeValue.toString());
                    attributesToRelease.put(attributeName, attributeValue);
                }
            }
        }

        this.logger.debug("Received {} attributes. Filtered and released {}", givenAttributes.size(), attributesToRelease.size());
        return attributesToRelease;
    }

    private Map<String, String> filterMapAttributes(Map<String, String> valuesToFilter) {
        Map<String, String> attributesToFilter = new HashMap(valuesToFilter.size());
        Iterator var3 = valuesToFilter.keySet().iterator();

        while(var3.hasNext()) {
            String attributeName = (String)var3.next();
            String attributeValue = (String)valuesToFilter.get(attributeName);
            if (this.patternMatchesAttributeValue(attributeValue)) {
                this.logReleasedAttributeEntry(attributeName, attributeValue);
                attributesToFilter.put(attributeName, valuesToFilter.get(attributeName));
            }
        }

        return attributesToFilter;
    }

    private boolean patternMatchesAttributeValue(String value) {
        return this.pattern.matcher(value).matches();
    }

    private String[] filterArrayAttributes(String[] valuesToFilter, String attributeName) {
        Vector<String> vector = new Vector(valuesToFilter.length);
        String[] var4 = valuesToFilter;
        int var5 = valuesToFilter.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String attributeValue = var4[var6];
            if (this.patternMatchesAttributeValue(attributeValue)) {
                this.logReleasedAttributeEntry(attributeName, attributeValue);
                vector.add(attributeValue);
            }
        }

        return (String[])vector.toArray(new String[0]);
    }

    private void logReleasedAttributeEntry(String attributeName, String attributeValue) {
        this.logger.debug("The attribute value [{}] for attribute name {} matches the pattern {}. Releasing attribute...", new Object[]{attributeValue, attributeName, this.pattern.pattern()});
    }
}
