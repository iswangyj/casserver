package org.jasig.cas.authentication;

import javax.validation.constraints.Size;

/**
 * @author SxL
 * Created on 9/25/2018 2:08 PM.
 */
public class UmeUsernamePasswordCredential extends UsernamePasswordCredential {
    @Size(
            message = "required.bSpecial"
    )
    private String bSpecial;

    public UmeUsernamePasswordCredential(String userName, String password, String bSpecial) {
        super(userName, password);
        this.bSpecial = bSpecial;
    }

    public String getbSpecial() {
        return this.bSpecial;
    }

    public void setbSpecial(String bSpecial) {
        this.bSpecial = bSpecial;
    }
}
