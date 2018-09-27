package org.jasig.cas.services;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author SxL
 * Created on 9/25/2018 3:15 PM.
 */
public final class InMemoryServiceRegistryDaoImpl implements ServiceRegistryDao {
    @NotNull
    private List<RegisteredService> registeredServices = new ArrayList();

    public InMemoryServiceRegistryDaoImpl() {
    }

    @Override
    public boolean delete(RegisteredService registeredService) {
        return this.registeredServices.remove(registeredService);
    }

    @Override
    public RegisteredService findServiceById(long id) {
        Iterator var3 = this.registeredServices.iterator();

        RegisteredService r;
        do {
            if (!var3.hasNext()) {
                return null;
            }

            r = (RegisteredService)var3.next();
        } while(r.getId() != id);

        return r;
    }

    @Override
    public List<RegisteredService> load() {
        return this.registeredServices;
    }

    @Override
    public RegisteredService save(RegisteredService registeredService) {
        if (registeredService.getId() == -9223372036854775807L) {
            ((AbstractRegisteredService)registeredService).setId(this.findHighestId() + 1L);
        }

        this.registeredServices.remove(registeredService);
        this.registeredServices.add(registeredService);
        return registeredService;
    }

    public void setRegisteredServices(List<RegisteredService> registeredServices) {
        this.registeredServices = registeredServices;
    }

    private long findHighestId() {
        long id = 0L;
        Iterator var3 = this.registeredServices.iterator();

        while(var3.hasNext()) {
            RegisteredService r = (RegisteredService)var3.next();
            if (r.getId() > id) {
                id = r.getId();
            }
        }

        return id;
    }
}
