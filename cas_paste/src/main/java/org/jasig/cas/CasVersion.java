package org.jasig.cas;

/**
 * @author SxL
 * Created on 9/25/2018 1:41 PM.
 */
public class CasVersion {
    private CasVersion() {
    }

    public static String getVersion() {
        return CasVersion.class.getPackage().getImplementationVersion();
    }
}
