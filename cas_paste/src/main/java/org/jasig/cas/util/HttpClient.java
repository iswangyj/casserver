package org.jasig.cas.util;

import java.net.URL;

/**
 * @author SxL
 * Created on 9/25/2018 5:12 PM.
 */
public interface HttpClient {
    boolean sendMessageToEndPoint(String var1, String var2, boolean var3);

    boolean isValidEndPoint(String var1);

    boolean isValidEndPoint(URL var1);
}
