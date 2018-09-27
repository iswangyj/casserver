package org.jasig.cas.util;

/**
 * @author SxL
 * Created on 9/25/2018 5:12 PM.
 */
public interface NumericGenerator {
    String getNextNumberAsString();

    int maxLength();

    int minLength();
}
