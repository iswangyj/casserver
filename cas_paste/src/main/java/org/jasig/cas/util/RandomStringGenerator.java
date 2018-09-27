package org.jasig.cas.util;

/**
 * @author SxL
 * Created on 9/25/2018 5:14 PM.
 */
public interface RandomStringGenerator {
    int getMinLength();

    int getMaxLength();

    String getNewString();

    byte[] getNewStringAsBytes();
}

