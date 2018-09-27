package org.jasig.cas.authentication;

import org.springframework.util.Assert;

/**
 * @author SxL
 * Created on 9/25/2018 2:07 PM.
 */
public abstract class RootCasException extends Exception {
    private static final long serialVersionUID = -2384466176716541689L;
    private String code;

    public RootCasException(String code) {
        this.initException(code);
    }

    public RootCasException(String code, String msg) {
        super(msg);
        this.initException(code);
    }

    public RootCasException(String code, Throwable throwable) {
        super(throwable);
        this.initException(code);
    }

    public final String getCode() {
        Throwable cause = this.getCause();
        return cause != null && cause instanceof RootCasException ? ((RootCasException)cause).getCode() : this.code;
    }

    private void initException(String code) {
        Assert.hasLength(code, "The exception code cannot be blank");
        this.code = code;
    }

    @Override
    public String toString() {
        return this.getCode();
    }
}
