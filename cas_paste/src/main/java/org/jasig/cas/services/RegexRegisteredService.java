package org.jasig.cas.services;

import org.jasig.cas.authentication.principal.Service;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.regex.Pattern;

/**
 * @author SxL
 * Created on 9/25/2018 3:17 PM.
 */

@Entity
@DiscriminatorValue("regex")
public class RegexRegisteredService extends AbstractRegisteredService {
    private static final long serialVersionUID = -8258660210826975771L;
    private transient Pattern servicePattern;

    public RegexRegisteredService() {
    }

    @Override
    public void setServiceId(String id) {
        this.serviceId = id;
    }

    @Override
    public boolean matches(Service service) {
        if (this.servicePattern == null) {
            this.servicePattern = this.createPattern(this.serviceId);
        }

        return service != null && this.servicePattern.matcher(service.getId()).matches();
    }

    @Override
    protected AbstractRegisteredService newInstance() {
        return new RegexRegisteredService();
    }

    private Pattern createPattern(String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern cannot be null.");
        } else {
            return Pattern.compile(pattern);
        }
    }
}