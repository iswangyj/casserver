package org.jasig.cas.authentication.principal;

import org.jasig.cas.authentication.Credential;
import org.jasig.services.persondir.IPersonAttributeDao;
import org.jasig.services.persondir.IPersonAttributes;
import org.jasig.services.persondir.support.StubPersonAttributeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author SxL
 * Created on 9/25/2018 2:45 PM.
 */
public class PersonDirectoryPrincipalResolver implements PrincipalResolver {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private boolean returnNullIfNoAttributes = false;
    @NotNull
    private IPersonAttributeDao attributeRepository = new StubPersonAttributeDao(new HashMap());
    private String principalAttributeName;

    public PersonDirectoryPrincipalResolver() {
    }

    @Override
    public boolean supports(Credential credential) {
        return true;
    }

    @Override
    public final Principal resolve(Credential credential) {
        this.logger.debug("Attempting to resolve a principal...");
        String principalId = this.extractPrincipalId(credential);
        if (principalId == null) {
            this.logger.debug("Got null for extracted principal ID; returning null.");
            return null;
        } else {
            this.logger.debug("Creating SimplePrincipal for [{}]", principalId);
            IPersonAttributes personAttributes = this.attributeRepository.getPerson(principalId);
            Map attributes;
            if (personAttributes == null) {
                attributes = null;
            } else {
                attributes = personAttributes.getAttributes();
            }

            if (attributes != null && !attributes.isEmpty()) {
                Map<String, Object> convertedAttributes = new HashMap();
                Iterator var6 = attributes.keySet().iterator();

                while(var6.hasNext()) {
                    String key = (String)var6.next();
                    List<Object> values = (List)attributes.get(key);
                    if (key.equalsIgnoreCase(this.principalAttributeName)) {
                        if (values.isEmpty()) {
                            this.logger.debug("{} is empty, using {} for principal", this.principalAttributeName, principalId);
                        } else {
                            principalId = values.get(0).toString();
                            this.logger.debug("Found principal attribute value {}; removing {} from attribute map.", principalId, this.principalAttributeName);
                        }
                    } else {
                        convertedAttributes.put(key, values.size() == 1 ? values.get(0) : values);
                    }
                }

                return new SimplePrincipal(principalId, convertedAttributes);
            } else {
                return !this.returnNullIfNoAttributes ? new SimplePrincipal(principalId) : null;
            }
        }
    }

    public final void setAttributeRepository(IPersonAttributeDao attributeRepository) {
        this.attributeRepository = attributeRepository;
    }

    public void setReturnNullIfNoAttributes(boolean returnNullIfNoAttributes) {
        this.returnNullIfNoAttributes = returnNullIfNoAttributes;
    }

    public void setPrincipalAttributeName(String attribute) {
        this.principalAttributeName = attribute;
    }

    protected String extractPrincipalId(Credential credential) {
        return credential.getId();
    }
}
