package org.jasig.cas.authentication.principal;

/**
 * @author SxL
 * Created on 9/25/2018 2:50 PM.
 */
public interface SingleLogoutService extends WebApplicationService {
    boolean isLoggedOutAlready();

    void setLoggedOutAlready(boolean var1);
}
