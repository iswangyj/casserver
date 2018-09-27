package com.ume.framework.plugin.sessionman;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

/**
 * @author SxL
 * Created on 9/25/2018 1:24 PM.
 */
public class SessionListener {
    private static final long serialVersionUID = 1L;
    private static Logger log = Logger.getLogger(SessionListener.class);

    public SessionListener() {
    }

    public void sessionCreated(HttpSessionEvent arg0) {
        try {
            HttpSession session = arg0.getSession();
            SessionManager.getInstance().addSession(session);
        } catch (Exception var3) {
            log.error(var3);
        }

    }

    public void sessionDestroyed(HttpSessionEvent event) {
        String sessionID = event.getSession().getId();

        try {
            SessionManager.getInstance().removeSession(sessionID);
        } catch (Exception var4) {
            log.error("Session注销时出现异常！");
            log.error(var4);
        }

    }
}
