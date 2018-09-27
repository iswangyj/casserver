package org.jasig.cas.services.jmx;

import org.jasig.cas.services.ServicesManager;
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
public final class ServicesManagerMBean extends AbstractServicesManagerMBean<ServicesManager> {
    public ServicesManagerMBean(ServicesManager servicesManager) {
        super(servicesManager);
    }
}
