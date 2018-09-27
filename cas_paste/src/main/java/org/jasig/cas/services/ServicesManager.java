package org.jasig.cas.services;

import org.jasig.cas.authentication.principal.Service;

import java.util.Collection;

/**
 * @author SxL
 * Created on 9/25/2018 3:20 PM.
 */
public interface ServicesManager {
    RegisteredService save(RegisteredService var1);

    RegisteredService delete(long var1);

    RegisteredService findServiceBy(Service var1);

    RegisteredService findServiceBy(long var1);

    Collection<RegisteredService> getAllServices();

    boolean matchesExistingService(Service var1);
}
