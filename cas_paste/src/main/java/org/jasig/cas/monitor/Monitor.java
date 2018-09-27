package org.jasig.cas.monitor;

/**
 * @author SxL
 * Created on 9/25/2018 3:00 PM.
 */
public interface Monitor<S extends Status> {
    String getName();

    S observe();
}

