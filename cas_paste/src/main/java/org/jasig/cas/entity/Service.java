package org.jasig.cas.entity;

import java.util.Date;

/**
 * @author SxL
 * Created on 7/6/2018 2:44 PM.
 */
public class Service {
    private Integer id;

    private Provider provider;

    private String privilegeSymbol;

    private String name;

    private String symbol;

    private String url;

    private Boolean enabled;

    private Date createAt;

    private Date updateAt;

    private Date deleteAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public String getPrivilegeSymbol() {
        return privilegeSymbol;
    }

    public void setPrivilegeSymbol(String privilegeSymbol) {
        this.privilegeSymbol = privilegeSymbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Date getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(Date deleteAt) {
        this.deleteAt = deleteAt;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", provider=" + provider +
                ", privilegeSymbol='" + privilegeSymbol + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", enabled=" + enabled +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                ", deleteAt=" + deleteAt +
                '}';
    }
}