package org.jasig.cas.services;

import org.jasig.cas.authentication.principal.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author SxL
 * Created on 9/25/2018 3:18 PM.
 */
public interface RegisteredService extends Cloneable, Serializable {
    long INITIAL_IDENTIFIER_VALUE = -9223372036854775807L;

    boolean isEnabled();

    boolean isAnonymousAccess();

    boolean isIgnoreAttributes();

    List<String> getAllowedAttributes();

    boolean isAllowedToProxy();

    String getServiceId();

    long getId();

    String getName();

    String getTheme();

    boolean isSsoEnabled();

    String getDescription();

    int getEvaluationOrder();

    void setEvaluationOrder(int var1);

    String getUsernameAttribute();

    Set<String> getRequiredHandlers();

    boolean matches(Service var1);

    RegisteredService clone() throws CloneNotSupportedException;

    RegisteredServiceAttributeFilter getAttributeFilter();

    LogoutType getLogoutType();
}