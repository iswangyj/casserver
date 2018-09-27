package org.jasig.cas.monitor;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @author SxL
 * Created on 9/25/2018 2:59 PM.
 */
public class HealthCheckMonitor implements Monitor<HealthStatus> {
    @NotNull
    private Collection<Monitor> monitors = Collections.emptySet();

    public HealthCheckMonitor() {
    }

    public void setMonitors(Collection<Monitor> monitors) {
        this.monitors = monitors;
    }

    @Override
    public String getName() {
        return HealthCheckMonitor.class.getSimpleName();
    }

    @Override
    public HealthStatus observe() {
        Map<String, Status> results = new LinkedHashMap(this.monitors.size());
        StatusCode code = StatusCode.UNKNOWN;

        Status result;
        Monitor monitor;
        for(Iterator var4 = this.monitors.iterator(); var4.hasNext(); results.put(monitor.getName(), result)) {
            monitor = (Monitor)var4.next();

            try {
                result = monitor.observe();
                if (result.getCode().value() > code.value()) {
                    code = result.getCode();
                }
            } catch (Exception var7) {
                code = StatusCode.ERROR;
                result = new Status(code, var7.getClass().getSimpleName() + ": " + var7.getMessage());
            }
        }

        return new HealthStatus(code, results);
    }
}
