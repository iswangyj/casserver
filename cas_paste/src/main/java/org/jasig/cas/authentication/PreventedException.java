package org.jasig.cas.authentication;

/**
 * @author SxL
 * Created on 9/25/2018 2:04 PM.
 */
public class PreventedException extends Exception {
    private static final long serialVersionUID = 4702274165911620708L;

    public PreventedException(Throwable cause) {
        super(cause);
    }

    public PreventedException(String message, Throwable cause) {
        super(message, cause);
    }
}
