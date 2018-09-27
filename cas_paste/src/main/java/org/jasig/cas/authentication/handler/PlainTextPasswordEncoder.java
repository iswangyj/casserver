package org.jasig.cas.authentication.handler;

/**
 * @author SxL
 * Created on 9/25/2018 2:14 PM.
 */
public final class PlainTextPasswordEncoder implements PasswordEncoder {
    public PlainTextPasswordEncoder() {
    }

    public String encode(String password) {
        return password;
    }
}
