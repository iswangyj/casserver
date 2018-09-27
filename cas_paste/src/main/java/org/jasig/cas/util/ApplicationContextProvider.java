package org.jasig.cas.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author SxL
 * Created on 9/25/2018 5:08 PM.
 */
public class ApplicationContextProvider implements ApplicationContextAware {
    private static ApplicationContext CONTEXT = null;

    public ApplicationContextProvider() {
    }

    public static ApplicationContext getApplicationContext() {
        return CONTEXT;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        CONTEXT = ctx;
    }
}
