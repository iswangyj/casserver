package org.jasig.cas.services;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.*;
import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * @author SxL
 * Created on 9/25/2018 3:09 PM.
 */
@Entity
@Inheritance
@DiscriminatorColumn(
        name = "expression_type",
        length = 15,
        discriminatorType = DiscriminatorType.STRING,
        columnDefinition = "VARCHAR(15) DEFAULT 'ant'"
)
@Table(
        name = "RegisteredServiceImpl"
)
public abstract class AbstractRegisteredService implements RegisteredService, Comparable<RegisteredService>, Serializable {
    private static final long serialVersionUID = 7645279151115635245L;
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private long id = -9223372036854775807L;
    @ElementCollection(
            targetClass = String.class,
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "rs_attributes",
            joinColumns = {@JoinColumn(
                    name = "RegisteredServiceImpl_id"
            )}
    )
    @Column(
            name = "a_name",
            nullable = false
    )
    @IndexColumn(
            name = "a_id"
    )
    private List<String> allowedAttributes = new ArrayList();
    @Column(
            length = 255,
            updatable = true,
            insertable = true,
            nullable = false
    )
    private String description;
    @Column(
            length = 255,
            updatable = true,
            insertable = true,
            nullable = false
    )
    protected String serviceId;
    @Column(
            length = 255,
            updatable = true,
            insertable = true,
            nullable = false
    )
    private String name;
    @Column(
            length = 255,
            updatable = true,
            insertable = true,
            nullable = true
    )
    private String theme;
    private boolean allowedToProxy = false;
    private boolean enabled = true;
    private boolean ssoEnabled = true;
    private boolean anonymousAccess = false;
    private boolean ignoreAttributes = false;
    @Column(
            name = "evaluation_order",
            nullable = false
    )
    private int evaluationOrder;
    @Transient
    private RegisteredServiceAttributeFilter attributeFilter = null;
    @Column(
            name = "username_attr",
            nullable = true,
            length = 256
    )
    private String usernameAttribute = null;
    @Transient
    private LogoutType logoutType;
    @Lob
    @Column(
            name = "required_handlers"
    )
    private HashSet<String> requiredHandlers;

    public AbstractRegisteredService() {
        this.logoutType = LogoutType.BACK_CHANNEL;
        this.requiredHandlers = new HashSet();
    }

    @Override
    public boolean isAnonymousAccess() {
        return this.anonymousAccess;
    }

    public void setAnonymousAccess(boolean anonymousAccess) {
        this.anonymousAccess = anonymousAccess;
    }

    @Override

    public List<String> getAllowedAttributes() {
        return this.allowedAttributes;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getServiceId() {
        return this.serviceId;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getTheme() {
        return this.theme;
    }

    @Override
    public boolean isAllowedToProxy() {
        return this.allowedToProxy;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public boolean isSsoEnabled() {
        return this.ssoEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (this == o) {
            return true;
        } else if (!(o instanceof AbstractRegisteredService)) {
            return false;
        } else {
            AbstractRegisteredService that = (AbstractRegisteredService)o;
            return (new EqualsBuilder()).append(this.allowedToProxy, that.allowedToProxy).append(this.anonymousAccess, that.anonymousAccess).append(this.enabled, that.enabled).append(this.evaluationOrder, that.evaluationOrder).append(this.ignoreAttributes, that.ignoreAttributes).append(this.ssoEnabled, that.ssoEnabled).append(this.allowedAttributes, that.allowedAttributes).append(this.description, that.description).append(this.name, that.name).append(this.serviceId, that.serviceId).append(this.theme, that.theme).append(this.usernameAttribute, that.usernameAttribute).append(this.logoutType, that.logoutType).isEquals();
        }
    }

    @Override
    public int hashCode() {
        return (new HashCodeBuilder(7, 31)).append(this.allowedAttributes).append(this.description).append(this.serviceId).append(this.name).append(this.theme).append(this.enabled).append(this.ssoEnabled).append(this.anonymousAccess).append(this.ignoreAttributes).append(this.evaluationOrder).append(this.usernameAttribute).append(this.logoutType).toHashCode();
    }

    public void setAllowedAttributes(List<String> allowedAttributes) {
        if (allowedAttributes == null) {
            this.allowedAttributes = new ArrayList();
        } else {
            this.allowedAttributes = allowedAttributes;
        }

    }

    public void setAllowedToProxy(boolean allowedToProxy) {
        this.allowedToProxy = allowedToProxy;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public abstract void setServiceId(String var1);

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSsoEnabled(boolean ssoEnabled) {
        this.ssoEnabled = ssoEnabled;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    @Override
    public boolean isIgnoreAttributes() {
        return this.ignoreAttributes;
    }

    public void setIgnoreAttributes(boolean ignoreAttributes) {
        this.ignoreAttributes = ignoreAttributes;
    }

    @Override
    public void setEvaluationOrder(int evaluationOrder) {
        this.evaluationOrder = evaluationOrder;
    }

    @Override
    public int getEvaluationOrder() {
        return this.evaluationOrder;
    }

    @Override
    public String getUsernameAttribute() {
        return this.usernameAttribute;
    }

    public void setUsernameAttribute(String username) {
        if (StringUtils.isBlank(username)) {
            this.usernameAttribute = null;
        } else {
            this.usernameAttribute = username;
        }

    }

    @Override
    public final LogoutType getLogoutType() {
        return this.logoutType;
    }

    public final void setLogoutType(LogoutType logoutType) {
        this.logoutType = logoutType;
    }

    @Override
    public RegisteredService clone() throws CloneNotSupportedException {
        AbstractRegisteredService clone = this.newInstance();
        clone.copyFrom(this);
        return clone;
    }

    public void copyFrom(RegisteredService source) {
        this.setId(source.getId());
        this.setAllowedAttributes(new ArrayList(source.getAllowedAttributes()));
        this.setAllowedToProxy(source.isAllowedToProxy());
        this.setDescription(source.getDescription());
        this.setEnabled(source.isEnabled());
        this.setName(source.getName());
        this.setServiceId(source.getServiceId());
        this.setSsoEnabled(source.isSsoEnabled());
        this.setTheme(source.getTheme());
        this.setAnonymousAccess(source.isAnonymousAccess());
        this.setIgnoreAttributes(source.isIgnoreAttributes());
        this.setEvaluationOrder(source.getEvaluationOrder());
        this.setUsernameAttribute(source.getUsernameAttribute());
        this.setLogoutType(source.getLogoutType());
    }

    @Override
    public int compareTo(RegisteredService other) {
        return (new CompareToBuilder()).append(this.getEvaluationOrder(), other.getEvaluationOrder()).append(this.getName().toLowerCase(), other.getName().toLowerCase()).append(this.getServiceId(), other.getServiceId()).toComparison();
    }

    @Override
    public String toString() {
        ToStringBuilder toStringBuilder = new ToStringBuilder((Object)null, ToStringStyle.SHORT_PREFIX_STYLE);
        toStringBuilder.append("id", this.id);
        toStringBuilder.append("name", this.name);
        toStringBuilder.append("description", this.description);
        toStringBuilder.append("serviceId", this.serviceId);
        toStringBuilder.append("usernameAttribute", this.usernameAttribute);
        toStringBuilder.append("attributes", this.allowedAttributes.toArray());
        return toStringBuilder.toString();
    }

    protected abstract AbstractRegisteredService newInstance();

    public final void setAttributeFilter(RegisteredServiceAttributeFilter filter) {
        this.attributeFilter = filter;
    }

    @Override
    public RegisteredServiceAttributeFilter getAttributeFilter() {
        return this.attributeFilter;
    }

    @Override
    public Set<String> getRequiredHandlers() {
        if (this.requiredHandlers == null) {
            this.requiredHandlers = new HashSet();
        }

        return this.requiredHandlers;
    }

    public void setRequiredHandlers(Set<String> handlers) {
        this.getRequiredHandlers().clear();
        if (handlers != null) {
            Iterator var2 = handlers.iterator();

            while(var2.hasNext()) {
                String handler = (String)var2.next();
                this.getRequiredHandlers().add(handler);
            }

        }
    }
}
