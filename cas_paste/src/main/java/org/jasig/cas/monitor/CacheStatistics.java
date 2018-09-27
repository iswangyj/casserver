package org.jasig.cas.monitor;

/**
 * @author SxL
 * Created on 9/25/2018 2:57 PM.
 */
public interface CacheStatistics {
    long getSize();

    long getCapacity();

    long getEvictions();

    int getPercentFree();

    String getName();

    void toString(StringBuilder var1);
}
