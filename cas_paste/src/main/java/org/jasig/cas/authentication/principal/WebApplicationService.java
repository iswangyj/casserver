package org.jasig.cas.authentication.principal;

/**
 * @author SxL
 * Created on 9/25/2018 2:51 PM.
 */
public interface WebApplicationService extends Service {
    Response getResponse(String var1);

    String getArtifactId();

    String getOriginalUrl();
}