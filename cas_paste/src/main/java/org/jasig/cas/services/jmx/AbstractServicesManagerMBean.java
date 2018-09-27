package org.jasig.cas.services.jmx;

import org.jasig.cas.services.RegisteredService;
import org.jasig.cas.services.RegisteredServiceImpl;
import org.jasig.cas.services.ServicesManager;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author SxL
 * Created on 9/25/2018 3:21 PM.
 */
public abstract class AbstractServicesManagerMBean<T extends ServicesManager> {
    @NotNull
    private T servicesManager;

    protected AbstractServicesManagerMBean(T svcMgr) {
        this.servicesManager = svcMgr;
    }

    protected final T getServicesManager() {
        return this.servicesManager;
    }

    @ManagedAttribute(
            description = "Retrieves the list of Registered Services in a slightly friendlier output."
    )
    public final List<String> getRegisteredServicesAsStrings() {
        List<String> services = new ArrayList();
        Iterator var2 = this.servicesManager.getAllServices().iterator();

        while(var2.hasNext()) {
            RegisteredService r = (RegisteredService)var2.next();
            services.add("id: " + r.getId() + " name: " + r.getName() + " enabled: " + r.isEnabled() + " ssoEnabled: " + r.isSsoEnabled() + " serviceId: " + r.getServiceId());
        }

        return services;
    }

    @ManagedOperation(
            description = "Can remove a service based on its identifier."
    )
    @ManagedOperationParameter(
            name = "id",
            description = "the identifier to remove"
    )
    public final RegisteredService removeService(long id) {
        return this.servicesManager.delete(id);
    }

    @ManagedOperation(
            description = "Disable a service by id."
    )
    @ManagedOperationParameter(
            name = "id",
            description = "the identifier to disable"
    )
    public final void disableService(long id) {
        this.changeEnabledState(id, false);
    }

    @ManagedOperation(
            description = "Enable a service by its id."
    )
    @ManagedOperationParameter(
            name = "id",
            description = "the identifier to enable."
    )
    public final void enableService(long id) {
        this.changeEnabledState(id, true);
    }

    private void changeEnabledState(long id, boolean newState) {
        RegisteredService r = this.servicesManager.findServiceBy(id);
        Assert.notNull(r, "invalid RegisteredService id");
        ((RegisteredServiceImpl)r).setEnabled(newState);
        this.servicesManager.save(r);
    }
}

