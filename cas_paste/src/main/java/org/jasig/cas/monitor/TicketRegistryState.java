package org.jasig.cas.monitor;

/**
 * @author SxL
 * Created on 9/25/2018 3:03 PM.
 */
public interface TicketRegistryState {
    int sessionCount();

    int serviceTicketCount();
}
