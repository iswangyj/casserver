package org.jasig.cas.authentication.principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author SxL
 * Created on 9/25/2018 2:38 PM.
 */
public abstract class AbstractWebApplicationService implements SingleLogoutService {
    private static final long serialVersionUID = 610105280927740076L;
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractWebApplicationService.class);
    private static final Map<String, Object> EMPTY_MAP = Collections.unmodifiableMap(new HashMap());
    private final String id;
    private final String originalUrl;
    private final String artifactId;
    private Principal principal;
    private boolean loggedOutAlready = false;

    protected AbstractWebApplicationService(String id, String originalUrl, String artifactId) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.artifactId = artifactId;
    }

    @Override
    public final String toString() {
        return this.id;
    }

    @Override
    public final String getId() {
        return this.id;
    }

    @Override
    public final String getArtifactId() {
        return this.artifactId;
    }

    @Override
    public final Map<String, Object> getAttributes() {
        return EMPTY_MAP;
    }

    protected static String cleanupUrl(String url) {
        if (url == null) {
            return null;
        } else {
            int jsessionPosition = url.indexOf(";jsession");
            if (jsessionPosition == -1) {
                return url;
            } else {
                int questionMarkPosition = url.indexOf("?");
                return questionMarkPosition < jsessionPosition ? url.substring(0, url.indexOf(";jsession")) : url.substring(0, jsessionPosition) + url.substring(questionMarkPosition);
            }
        }
    }

    @Override
    public final String getOriginalUrl() {
        return this.originalUrl;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        } else if (object instanceof Service) {
            Service service = (Service) object;
            return this.getId().equals(service.getId());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        boolean prime = true;
        int result = 1;
        result = 41 * result + (this.id == null ? 0 : this.id.hashCode());
        return result;
    }

    protected Principal getPrincipal() {
        return this.principal;
    }

    @Override
    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }

    @Override
    public boolean matches(Service service) {
        return this.id.equals(service.getId());
    }

    @Override
    public boolean isLoggedOutAlready() {
        return this.loggedOutAlready;
    }

    @Override
    public final void setLoggedOutAlready(boolean loggedOutAlready) {
        this.loggedOutAlready = loggedOutAlready;
    }
}

