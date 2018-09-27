package org.jasig.cas.services;

import org.jasig.cas.authentication.principal.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author SxL
 * Created on 9/25/2018 3:18 PM.
 */
@Entity
@DiscriminatorValue("ant")
public class RegisteredServiceImpl extends AbstractRegisteredService {
    private static final long serialVersionUID = -5906102762271197627L;
    private static final PathMatcher PATH_MATCHER = new AntPathMatcher();

    public RegisteredServiceImpl() {
    }

    @Override
    public void setServiceId(String id) {
        this.serviceId = id;
    }

    @Override
    public boolean matches(Service service) {
        return service != null && PATH_MATCHER.match(this.serviceId.toLowerCase(), service.getId().toLowerCase());
    }

    @Override
    protected AbstractRegisteredService newInstance() {
        return new RegisteredServiceImpl();
    }
}

