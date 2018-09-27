package org.jasig.cas.ticket.registry.support;

/**
 * @author SxL
 * Created on 9/25/2018 4:57 PM.
 */
public class NoOpLockingStrategy implements LockingStrategy {
    public NoOpLockingStrategy() {
    }

    @Override
    public boolean acquire() {
        return true;
    }

    @Override
    public void release() {
    }
}
