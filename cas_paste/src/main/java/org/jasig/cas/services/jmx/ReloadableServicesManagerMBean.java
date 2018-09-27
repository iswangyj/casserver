package org.jasig.cas.services.jmx;

import org.jasig.cas.services.ReloadableServicesManager;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * @author SxL
 * Created on 9/25/2018 3:22 PM.
 */
@ManagedResource(
        objectName = "CAS:name=JasigCasServicesManagerMBean",
        description = "Exposes the services management tool via JMX",
        log = true,
        logFile = "jasig_cas_jmx.logger",
        currencyTimeLimit = 15
)
public final class ReloadableServicesManagerMBean extends AbstractServicesManagerMBean<ReloadableServicesManager> {
    public ReloadableServicesManagerMBean(ReloadableServicesManager reloadableServicesManager) {
        super(reloadableServicesManager);
    }

    @ManagedOperation(
            description = "Reloads the list of the services from the persistence storage."
    )
    public void reload() {
        ((ReloadableServicesManager)this.getServicesManager()).reload();
    }
}

