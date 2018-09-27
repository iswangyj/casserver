package org.jasig.cas.logout;

/**
 * @author SxL
 * Created on 9/25/2018 2:54 PM.
 */
public enum  LogoutRequestStatus {
    NOT_ATTEMPTED,
    FAILURE,
    SUCCESS;

    private LogoutRequestStatus() {
    }
}
