package org.jasig.cas.services;

import java.util.Map;

/**
 * @author SxL
 * Created on 9/25/2018 3:18 PM.
 */
public interface RegisteredServiceAttributeFilter {
    Map<String, Object> filter(String var1, Map<String, Object> var2, RegisteredService var3);
}
