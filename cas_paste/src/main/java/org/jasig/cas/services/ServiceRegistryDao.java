package org.jasig.cas.services;

import java.util.List;

/**
 * @author SxL
 * Created on 9/25/2018 3:20 PM.
 */
public interface ServiceRegistryDao {
    RegisteredService save(RegisteredService var1);

    boolean delete(RegisteredService var1);

    List<RegisteredService> load();

    RegisteredService findServiceById(long var1);
}
