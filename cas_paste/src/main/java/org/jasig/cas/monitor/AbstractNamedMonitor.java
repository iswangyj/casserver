package org.jasig.cas.monitor;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * @author SxL
 * Created on 9/25/2018 2:56 PM.
 */
public abstract class AbstractNamedMonitor<S extends Status> implements Monitor<S> {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected String name;

    public AbstractNamedMonitor() {
    }

    public String getName() {
        return StringUtils.defaultIfEmpty(this.name, this.getClass().getSimpleName());
    }

    public void setName(String n) {
        Assert.hasText(n, "Monitor name cannot be null or empty.");
        this.name = n;
    }
}
