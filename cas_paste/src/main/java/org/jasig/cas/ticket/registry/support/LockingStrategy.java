package org.jasig.cas.ticket.registry.support;

/**
 * @author SxL
 * Created on 9/25/2018 4:57 PM.
 */
public interface LockingStrategy {
    boolean acquire();

    void release();
}
