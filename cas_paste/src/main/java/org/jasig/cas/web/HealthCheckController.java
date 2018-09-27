package org.jasig.cas.web;

import org.jasig.cas.monitor.HealthCheckMonitor;
import org.jasig.cas.monitor.HealthStatus;
import org.jasig.cas.monitor.Status;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Iterator;
import java.util.Map;

/**
 * @author SxL
 * Created on 9/25/2018 5:21 PM.
 */
public class HealthCheckController extends AbstractController {
    private static final String HEADER_PREFIX = "X-CAS-";
    @NotNull
    private HealthCheckMonitor healthCheckMonitor;

    public HealthCheckController() {
    }

    public void setHealthCheckMonitor(HealthCheckMonitor monitor) {
        this.healthCheckMonitor = monitor;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HealthStatus healthStatus = this.healthCheckMonitor.observe();
        StringBuilder sb = new StringBuilder();
        sb.append("Health: ").append(healthStatus.getCode());
        int i = 0;
        Iterator var8 = healthStatus.getDetails().entrySet().iterator();

        while(var8.hasNext()) {
            Map.Entry<String, Status> entry = (Map.Entry)var8.next();
            String name = (String)entry.getKey();
            Status status = (Status)entry.getValue();
            response.addHeader("X-CAS-" + name, String.format("%s;%s", status.getCode(), status.getDescription()));
            StringBuilder var10000 = sb.append("\n\n\t");
            ++i;
            var10000.append(i).append('.').append(name).append(": ");
            sb.append(status.getCode());
            if (status.getDescription() != null) {
                sb.append(" - ").append(status.getDescription());
            }
        }

        response.setStatus(healthStatus.getCode().value());
        response.setContentType("text/plain");
        response.getOutputStream().write(sb.toString().getBytes(response.getCharacterEncoding()));
        return null;
    }
}
