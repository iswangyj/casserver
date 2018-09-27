package org.jasig.cas.authentication.principal;

import java.io.Serializable;
import java.util.Map;

/**
 * @author SxL
 * Created on 9/25/2018 2:46 PM.
 */
public interface Principal extends Serializable {
    String getId();

    Map<String, Object> getAttributes();
}
