package org.jasig.cas.authentication.principal;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Map;

/**
 * @author SxL
 * Created on 9/25/2018 2:49 PM.
 */
public class SimplePrincipal implements Principal {
    private static final long serialVersionUID = -1255260750151385796L;
    private final String id;
    private Map<String, Object> attributes;

    private SimplePrincipal() {
        this.id = null;
    }

    public SimplePrincipal(String id) {
        this(id, (Map)null);
    }

    public SimplePrincipal(String id, Map<String, Object> attributes) {
        Assert.notNull(id, "id cannot be null");
        this.id = id;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes == null ? Collections.emptyMap() : Collections.unmodifiableMap(this.attributes);
    }

    @Override
    public String toString() {
        return this.id;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(83, 31);
        builder.append(this.id);
        return builder.toHashCode();
    }

    @Override
    public final String getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof SimplePrincipal && ((SimplePrincipal)o).getId().equals(this.id);
    }
}
