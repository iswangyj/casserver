package org.jasig.cas.services;

import org.jasig.cas.authentication.principal.Service;

import javax.validation.constraints.NotNull;

/**
 * @author SxL
 * Created on 9/25/2018 3:19 PM.
 */
public class ServiceContext {
    @NotNull
    private final Service service;
    @NotNull
    private final RegisteredService registeredService;

    public ServiceContext(@NotNull Service service, @NotNull RegisteredService registeredService) {
        this.service = service;
        this.registeredService = registeredService;
        if (!registeredService.matches(service)) {
            throw new IllegalArgumentException("Registered service does not match given service.");
        }
    }

    public Service getService() {
        return this.service;
    }

    public RegisteredService getRegisteredService() {
        return this.registeredService;
    }
}

