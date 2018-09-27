package org.jasig.cas.authentication;

import org.springframework.util.Assert;

import java.net.URL;

/**
 * @author SxL
 * Created on 9/25/2018 1:59 PM.
 */
public class HttpBasedServiceCredential extends AbstractCredential {
    private static final long serialVersionUID = 1492607216336354503L;
    private final URL callbackUrl;
    private final String callbackUrlAsString;

    protected HttpBasedServiceCredential() {
        this.callbackUrl = null;
        this.callbackUrlAsString = null;
    }

    public HttpBasedServiceCredential(URL callbackUrl) {
        Assert.notNull(callbackUrl, "callbackUrl cannot be null");
        this.callbackUrl = callbackUrl;
        this.callbackUrlAsString = callbackUrl.toExternalForm();
    }

    @Override
    public String getId() {
        return this.callbackUrlAsString;
    }

    public final URL getCallbackUrl() {
        return this.callbackUrl;
    }

    @Override
    public int hashCode() {
        boolean prime = true;
        int result = 1;
        result = 31 * result + (this.callbackUrlAsString == null ? 0 : this.callbackUrlAsString.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            HttpBasedServiceCredential other = (HttpBasedServiceCredential)obj;
            if (this.callbackUrlAsString == null) {
                if (other.callbackUrlAsString != null) {
                    return false;
                }
            } else if (!this.callbackUrlAsString.equals(other.callbackUrlAsString)) {
                return false;
            }

            return true;
        }
    }
}
