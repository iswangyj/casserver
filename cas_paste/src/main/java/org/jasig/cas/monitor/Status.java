package org.jasig.cas.monitor;

/**
 * @author SxL
 * Created on 9/25/2018 3:03 PM.
 */
public class Status {
    public static final Status UNKNOWN;
    public static final Status OK;
    public static final Status INFO;
    public static final Status WARN;
    public static final Status ERROR;
    private final StatusCode code;
    private final String description;

    public Status(StatusCode code) {
        this(code, (String)null);
    }

    public Status(StatusCode code, String desc) {
        this.code = code;
        this.description = desc;
    }

    public StatusCode getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    static {
        UNKNOWN = new Status(StatusCode.UNKNOWN);
        OK = new Status(StatusCode.OK);
        INFO = new Status(StatusCode.INFO);
        WARN = new Status(StatusCode.WARN);
        ERROR = new Status(StatusCode.ERROR);
    }
}
