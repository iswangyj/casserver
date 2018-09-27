package org.jasig.cas.monitor;

import java.util.Collections;
import java.util.Map;

/**
 * @author SxL
 * Created on 9/25/2018 2:59 PM.
 */
public class HealthStatus extends Status {
    private final Map<String, Status> details;

    public HealthStatus(StatusCode code, Map<String, Status> detailMap) {
        super(code);
        this.details = Collections.unmodifiableMap(detailMap);
    }

    public Map<String, Status> getDetails() {
        return this.details;
    }
}
