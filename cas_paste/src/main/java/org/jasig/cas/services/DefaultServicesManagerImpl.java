package org.jasig.cas.services;

import com.github.inspektr.audit.annotation.Audit;
import org.jasig.cas.authentication.principal.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SxL
 * Created on 9/25/2018 3:11 PM.
 */
public final class DefaultServicesManagerImpl implements ReloadableServicesManager {
    private final Logger logger;
    @NotNull
    private ServiceRegistryDao serviceRegistryDao;
    private ConcurrentHashMap<Long, RegisteredService> services;

    public DefaultServicesManagerImpl(ServiceRegistryDao serviceRegistryDao) {
        this(serviceRegistryDao, new ArrayList());
    }

    public DefaultServicesManagerImpl(ServiceRegistryDao serviceRegistryDao, List<String> defaultAttributes) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.services = new ConcurrentHashMap();
        this.serviceRegistryDao = serviceRegistryDao;
        this.load();
    }

    @Transactional(
            readOnly = false
    )
    @Audit(
            action = "DELETE_SERVICE",
            actionResolverName = "DELETE_SERVICE_ACTION_RESOLVER",
            resourceResolverName = "DELETE_SERVICE_RESOURCE_RESOLVER"
    )
    @Override
    public synchronized RegisteredService delete(long id) {
        RegisteredService r = this.findServiceBy(id);
        if (r == null) {
            return null;
        } else {
            this.serviceRegistryDao.delete(r);
            this.services.remove(id);
            return r;
        }
    }

    @Override
    public RegisteredService findServiceBy(Service service) {
        Collection<RegisteredService> c = this.convertToTreeSet();
        Iterator var3 = c.iterator();

        RegisteredService r;
        do {
            if (!var3.hasNext()) {
                return null;
            }

            r = (RegisteredService)var3.next();
        } while(!r.matches(service));

        return r;
    }

    @Override
    public RegisteredService findServiceBy(long id) {
        RegisteredService r = (RegisteredService)this.services.get(id);

        try {
            return r == null ? null : r.clone();
        } catch (CloneNotSupportedException var5) {
            return r;
        }
    }

    protected TreeSet<RegisteredService> convertToTreeSet() {
        return new TreeSet(this.services.values());
    }

    @Override
    public Collection<RegisteredService> getAllServices() {
        return Collections.unmodifiableCollection(this.convertToTreeSet());
    }

    @Override
    public boolean matchesExistingService(Service service) {
        return this.findServiceBy(service) != null;
    }

    @Transactional(
            readOnly = false
    )
    @Audit(
            action = "SAVE_SERVICE",
            actionResolverName = "SAVE_SERVICE_ACTION_RESOLVER",
            resourceResolverName = "SAVE_SERVICE_RESOURCE_RESOLVER"
    )
    @Override
    public synchronized RegisteredService save(RegisteredService registeredService) {
        RegisteredService r = this.serviceRegistryDao.save(registeredService);
        this.services.put(r.getId(), r);
        return r;
    }

    @Override
    public void reload() {
        this.logger.info("Reloading registered services.");
        this.load();
    }

    private void load() {
        ConcurrentHashMap<Long, RegisteredService> localServices = new ConcurrentHashMap();
        Iterator var2 = this.serviceRegistryDao.load().iterator();

        while(var2.hasNext()) {
            RegisteredService r = (RegisteredService)var2.next();
            this.logger.debug("Adding registered service {}", r.getServiceId());
            localServices.put(r.getId(), r);
        }

        this.services = localServices;
        this.logger.info(String.format("Loaded %s services.", this.services.size()));
    }
}

