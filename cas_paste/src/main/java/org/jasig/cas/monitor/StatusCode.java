package org.jasig.cas.monitor;

/**
 * @author SxL
 * Created on 9/25/2018 3:03 PM.
 */
public enum StatusCode {
    ERROR(500),
    WARN(400),
    INFO(300),
    OK(200),
    UNKNOWN(100);

    private final int value;

    private StatusCode(int numericValue) {
        this.value = numericValue;
    }

    public int value() {
        return this.value;
    }
}
