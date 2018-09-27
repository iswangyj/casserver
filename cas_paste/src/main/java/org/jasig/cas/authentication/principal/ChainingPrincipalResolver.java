package org.jasig.cas.authentication.principal;

import org.jasig.cas.authentication.Credential;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Iterator;
import java.util.List;

/**
 * @author SxL
 * Created on 9/25/2018 2:39 PM.
 */
public class ChainingPrincipalResolver implements PrincipalResolver {
    @NotNull
    @Size(
            min = 1
    )
    private List<PrincipalResolver> chain;

    public ChainingPrincipalResolver() {
    }

    public void setChain(List<PrincipalResolver> chain) {
        this.chain = chain;
    }

    @Override
    public Principal resolve(Credential credential) {
        Principal result = null;
        Credential input = credential;

        PrincipalResolver resolver;
        for(Iterator var4 = this.chain.iterator(); var4.hasNext(); result = resolver.resolve((Credential)input)) {
            resolver = (PrincipalResolver)var4.next();
            if (result != null) {
                input = new ChainingPrincipalResolver.IdentifiableCredential(result.getId());
            }
        }

        return result;
    }

    @Override
    public boolean supports(Credential credential) {
        return ((PrincipalResolver)this.chain.get(0)).supports(credential);
    }

    static class IdentifiableCredential implements Credential {
        private final String id;

        public IdentifiableCredential(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return this.id;
        }
    }
}
