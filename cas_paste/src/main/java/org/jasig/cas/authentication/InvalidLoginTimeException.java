package org.jasig.cas.authentication;

import javax.security.auth.login.AccountException;
import java.io.Serializable;

/**
 * @author SxL
 * Created on 9/25/2018 2:02 PM.
 */
public class InvalidLoginTimeException extends AccountException implements Serializable {
    private static final long serialVersionUID = -6699752791525619208L;

    public InvalidLoginTimeException() {
    }

    public InvalidLoginTimeException(String message) {
        super(message);
    }
}
