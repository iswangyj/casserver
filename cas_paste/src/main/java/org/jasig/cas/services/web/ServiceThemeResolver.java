package org.jasig.cas.services.web;

import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.services.RegisteredService;
import org.jasig.cas.services.ServicesManager;
import org.jasig.cas.web.support.ArgumentExtractor;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.theme.AbstractThemeResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author SxL
 * Created on 9/25/2018 3:30 PM.
 */
public final class ServiceThemeResolver extends AbstractThemeResolver {
    private ServicesManager servicesManager;
    private List<ArgumentExtractor> argumentExtractors;
    private Map<Pattern, String> overrides = new HashMap();

    public ServiceThemeResolver() {
    }

    @Override
    public String resolveThemeName(HttpServletRequest request) {
        if (this.servicesManager == null) {
            return this.getDefaultThemeName();
        } else {
            Service service = WebUtils.getService(this.argumentExtractors, request);
            RegisteredService rService = this.servicesManager.findServiceBy(service);
            String userAgent = request.getHeader("User-Agent");
            if (userAgent == null) {
                return this.getDefaultThemeName();
            } else {
                Iterator var5 = this.overrides.entrySet().iterator();

                while(var5.hasNext()) {
                    Map.Entry<Pattern, String> entry = (Map.Entry)var5.next();
                    if (((Pattern)entry.getKey()).matcher(userAgent).matches()) {
                        request.setAttribute("isMobile", "true");
                        request.setAttribute("browserType", entry.getValue());
                        break;
                    }
                }

                return service != null && rService != null && StringUtils.hasText(rService.getTheme()) ? rService.getTheme() : this.getDefaultThemeName();
            }
        }
    }

    @Override
    public void setThemeName(HttpServletRequest request, HttpServletResponse response, String themeName) {
    }

    public void setServicesManager(ServicesManager servicesManager) {
        this.servicesManager = servicesManager;
    }

    public void setArgumentExtractors(List<ArgumentExtractor> argumentExtractors) {
        this.argumentExtractors = argumentExtractors;
    }

    public void setMobileBrowsers(Map<String, String> mobileOverrides) {
        this.overrides = new HashMap();
        Iterator var2 = mobileOverrides.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var2.next();
            this.overrides.put(Pattern.compile((String)entry.getKey()), entry.getValue());
        }

    }
}

