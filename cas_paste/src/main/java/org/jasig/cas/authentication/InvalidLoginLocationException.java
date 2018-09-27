package org.jasig.cas.authentication;

import javax.security.auth.login.AccountException;
import java.io.Serializable;

/**
 * @author SxL
 * Created on 9/25/2018 2:02 PM.
 */
public class InvalidLoginLocationException extends AccountException implements Serializable {
    private static final long serialVersionUID = 5745711263227480194L;

    public InvalidLoginLocationException() {
    }

    public InvalidLoginLocationException(String message) {
        super(message);
    }
}
