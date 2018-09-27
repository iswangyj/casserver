package com.ume.framework.plugin.sessionman;

import com.ume.framework.plugin.sessionman.structure.SessionData;
import com.ume.framework.plugin.sessionman.structure.Settings;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author SxL
 * Created on 9/25/2018 1:28 PM.
 */
public class SessionManager {
    public static final String UME_USER_KEY = "UME_USER_KEY";
    private static SessionManager sm = null;
    private static Logger log = Logger.getLogger(SessionManager.class);
    private static Map<String, SessionData> mapSession = new Hashtable();
    private static Map<String, Map<String, SessionData>> mapUserSession = new Hashtable();
    private static Map<String, Map<String, Map<String, SessionData>>> mapUserIpSession = new Hashtable();
    private Settings settings = null;
    private static boolean bShowMemoryContent = false;

    private SessionManager() {
        this.init();
    }

    private void init() {
        this.settings = new Settings(true, true, 0, 0);
    }

    public static SessionManager getInstance() {
        if (null == sm) {
            Map var0 = mapSession;
            synchronized(mapSession) {
                if (null == sm) {
                    sm = new SessionManager();
                }
            }
        }

        return sm;
    }

    void addSession(HttpServletRequest request) throws Exception {
        this.addSession(request.getSession());
    }

    void addSession(HttpSession session) throws Exception {
        log.info(Calendar.getInstance().getTime().toString() + " : create sessionID : " + session.getId());
        String sessionID = session.getId();
        boolean b = this.contains(sessionID);
        if (!b) {
            if (this.settings.getiAllSessionCount() > 0 && this.settings.getiAllSessionCount() <= mapSession.size()) {
                session.invalidate();
                throw new Exception("SessMan-110 session瓒呭嚭浜嗘渶澶ф暟閲忥紙" + this.settings.getiAllSessionCount() + "锛夐檺鍒讹紝璇风◢鍚庡啀璇曪紒");
            } else {
                SessionData sessionData = new SessionData();
                sessionData.setHs(session);
                String user = (String)session.getAttribute("UME_USER_KEY");
                if (null != user && !"".equals(user)) {
                    sessionData.setUser(user);
                }

                mapSession.put(session.getId(), sessionData);
            }
        }
    }

    public void removeSession(String sessionID) throws Exception {
        SessionData sessionData = (SessionData)mapSession.get(sessionID);
        if (null != sessionID) {
            mapSession.remove(sessionID);
            if (null != sessionData) {
                HttpSession session = sessionData.getHs();
                if (null != session) {
                    String user = sessionData.getUser();
                    sessionData.outputAliveTimeAsMinutes();
                    this.removeUserSession(user, sessionID);
                    String ip = sessionData.getIp();
                    this.removeUserIpSession(ip, sessionID);
                    session.invalidate();
                }
            }
        }
    }

    public void addUserInfo(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        if (null == session) {
            throw new Exception("#SessMan-131 Session涓嶅彲浠ヤ负绌猴紒");
        } else {
            String currUser = (String)session.getAttribute("UME_USER_KEY");
            if (null == currUser) {
                throw new Exception("#SessMan-135 Session涓\ue15f病鏈夌敤鎴蜂俊鎭\ue224紒");
            } else {
                String sessionID = session.getId();
                SessionData sessionData = (SessionData)mapSession.get(sessionID);
                if (null == sessionData) {
                    sessionData = new SessionData();
                    sessionData.setHs(session);
                    mapSession.put(session.getId(), sessionData);
                }

                String preUser = sessionData.getUser();
                sessionData.setUser(currUser);
                if (!currUser.equals(preUser)) {
                    this.removeUserSession(preUser, session.getId());
                }

                this.addUserSession(sessionData);
                String preIP = sessionData.getIp();
                String currIP = request.getRemoteAddr();
                sessionData.setIp(currIP);
                if (!currIP.equals(preIP)) {
                    this.removeUserIpSession(preIP, sessionID);
                }

                this.addUserIpSession(sessionData);
            }
        }
    }

    public void removeUserInfo(HttpSession session) throws Exception {
        if (null == session) {
            throw new Exception("#SessMan-169 Session涓嶅彲浠ヤ负绌猴紒");
        } else {
            String sessionID = session.getId();
            SessionData sessionData = (SessionData)mapSession.get(sessionID);
            if (null != sessionData) {
                String user = sessionData.getUser();
                this.removeUserSession(user, session.getId());
                String ip = sessionData.getIp();
                this.removeUserIpSession(ip, session.getId());
                sessionData.clear();
            }

        }
    }

    private void addUserSession(SessionData sessionData) throws Exception {
        Map<String, SessionData> mapCurrUserSessions = null;
        HttpSession session = sessionData.getHs();
        synchronized(this) {
            mapCurrUserSessions = (Map)mapUserSession.get(sessionData.getUser());
            if (null == mapCurrUserSessions) {
                mapCurrUserSessions = new Hashtable();
                mapUserSession.put(sessionData.getUser(), mapCurrUserSessions);
            }

            if (this.settings.getiUserSessionCount() > 0 && this.settings.getiUserSessionCount() <= ((Map)mapCurrUserSessions).size()) {
                this.setImedTimeout(session.getId());
                throw new Exception("#SessMan-236 褰撳墠鐢ㄦ埛鐨凷ession鏁伴噺瓒呭嚭浜嗗崟涓\ue046敤鎴稴ession鏁扮殑鏈�澶ф暟閲忥紙" + this.settings.getiUserSessionCount() + "锛夐檺鍒讹紝璇风◢鍚庡啀璇曪紒");
            }
        }

        ((Map)mapCurrUserSessions).put(session.getId(), sessionData);
    }

    private void removeUserSession(String user, String sessionID) throws Exception {
        if (null != user && !"".equals(user)) {
            Map<String, SessionData> mapUserSessions = (Map)mapUserSession.get(user);
            if (null != mapUserSessions) {
                mapUserSessions.remove(sessionID);
            }

        }
    }

    private void addUserIpSession(SessionData sessionData) throws Exception {
        String user = sessionData.getUser();
        Map<String, Map<String, SessionData>> mapIpSession = (Map)mapUserIpSession.get(user);
        if (null == mapIpSession) {
            mapIpSession = new Hashtable();
            mapUserIpSession.put(user, mapIpSession);
        }

        String ip = sessionData.getIp();
        if (this.settings.isbSingleAcount()) {
            boolean bExists = false;
            StringBuilder sb = new StringBuilder();
            synchronized(this) {
                if (((Map)mapIpSession).size() > 0) {
                    Set<String> keySet = ((Map)mapIpSession).keySet();
                    Iterator it = ((Map)mapIpSession).keySet().iterator();

                    while(it.hasNext()) {
                        String key = (String)it.next();
                        Map<String, SessionData> mapSessionOneIp = (Map)((Map)mapIpSession).get(key);
                        if (null == mapSessionOneIp) {
                            mapSessionOneIp = new Hashtable();
                            ((Map)mapIpSession).put(key, mapSessionOneIp);
                        }

                        if (ip.equals(key)) {
                            bExists = true;
                        } else if (((Map)mapSessionOneIp).size() > 0) {
                            if (sb.length() > 0) {
                                sb.append(",");
                            }

                            sb.append(key);
                        }
                    }

                    if (!bExists) {
                        throw new Exception("#SessMan-245 褰撳墠鐢ㄦ埛锛�" + user + "锛夊凡缁忓湪鏈哄櫒IP涓�" + sb.toString() + "鐨勫\ue179鎴风\ue06c鐧诲綍锛屼笉鍙\ue219互閲嶅\ue632鐧诲綍锛�");
                    }
                }
            }
        }

        Map<String, SessionData> mapSessionOneIp = (Map)((Map)mapIpSession).get(ip);
        if (null == mapSessionOneIp) {
            mapSessionOneIp = new Hashtable();
            ((Map)mapIpSession).put(ip, mapSessionOneIp);
        }

        ((Map)mapSessionOneIp).put(sessionData.getHs().getId(), sessionData);
    }

    private void removeUserIpSession(String ip, String sessionID) {
        if (null != ip && !"".equals(ip)) {
            SessionData sessionData = (SessionData)mapSession.get(sessionID);
            String user = sessionData.getUser();
            Map<String, Map<String, SessionData>> mapIpSession = (Map)mapUserIpSession.get(user);
            if (null != mapIpSession) {
                Map<String, SessionData> mapSessionOneIp = (Map)mapIpSession.get(ip);
                if (null != mapSessionOneIp) {
                    mapSessionOneIp.remove(sessionID);
                }
            }

        }
    }

    public String[] getUsersAccount() {
        String[] accounts = new String[mapUserSession.size()];
        mapUserSession.keySet().toArray(accounts);
        return accounts;
    }

    private boolean contains(String sessionID) {
        boolean bHas = false;
        SessionData sessionData = (SessionData)mapSession.get(sessionID);
        if (null != sessionData) {
            HttpSession session = sessionData.getHs();
            if (null != session) {
                bHas = true;
            }
        }

        return bHas;
    }

    private void setImedTimeout(String sessionID) {
        SessionData sessionData = (SessionData)mapSession.get(sessionID);
        if (null != sessionData) {
            HttpSession session = sessionData.getHs();
            if (null != session) {
                session.setMaxInactiveInterval(5);
            }

        }
    }

    private void setNormalTimeout(String sessionID) {
        SessionData sessionData = (SessionData)mapSession.get(sessionID);
        if (null != sessionData) {
            HttpSession session = sessionData.getHs();
            if (null != session) {
                session.setMaxInactiveInterval(1800);
            }

        }
    }

    public void setUser(HttpServletRequest request, String user) {
        request.getSession().setAttribute("UME_USER_KEY", user);
    }

    public static void setbShowMemoryContent(boolean bShowMemoryContent) {
        bShowMemoryContent = bShowMemoryContent;
    }

    public void showMemoryContent() {
        bShowMemoryContent = true;
        if (bShowMemoryContent) {
            StringBuilder sb = new StringBuilder();
            sb.append("Session 鎬绘暟锛�" + mapSession.size() + "\r\n");
            sb.append("鐢ㄦ埛Session鏁伴噺锛歕r\n");
            Iterator it;
            String user;
            Map mapData2;
            Iterator it2;
            String ip;
            if (null != mapUserSession && mapUserSession.size() > 0) {
                it = mapUserSession.keySet().iterator();

                label78:
                while(true) {
                    do {
                        do {
                            if (!it.hasNext()) {
                                break label78;
                            }

                            user = (String)it.next();
                            sb.append("鐢ㄦ埛鍚嶏細" + user + "   Session淇℃伅锛歕r\n");
                            mapData2 = (Map)mapUserSession.get(user);
                        } while(null == mapData2);
                    } while(mapData2.size() <= 0);

                    it2 = mapData2.keySet().iterator();

                    while(it2.hasNext()) {
                        ip = (String)it2.next();
                        SessionData sessionData = (SessionData)mapData2.get(ip);
                        sb.append("sessionID:" + ip + " info:(" + sessionData.showInfo() + ") \r\n");
                    }
                }
            }

            sb.append("鐢ㄦ埛瀹㈡埛绔\ue224紙IP锛夌殑session鏁伴噺锛歕r\n");
            if (null != mapUserIpSession && mapUserIpSession.size() > 0) {
                it = mapUserIpSession.keySet().iterator();

                label55:
                while(true) {
                    do {
                        do {
                            if (!it.hasNext()) {
                                break label55;
                            }

                            user = (String)it.next();
                            mapData2 = (Map)mapUserIpSession.get(user);
                        } while(null == mapData2);
                    } while(mapData2.size() <= 0);

                    it2 = mapData2.keySet().iterator();

                    while(it2.hasNext()) {
                        ip = (String)it2.next();
                        sb.append("鐢ㄦ埛鍚�(IP)锛�" + user + "(" + ip + ")   Session淇℃伅锛歕r\n");
                        Map<String, SessionData> mapData = (Map)mapData2.get(ip);
                        Iterator it1 = mapData.keySet().iterator();

                        while(it1.hasNext()) {
                            String sessionID = (String)it1.next();
                            SessionData sessionData = (SessionData)mapData.get(sessionID);
                            sb.append("sessionID:" + sessionID + " info:(" + sessionData.showInfo() + ") \r\n");
                        }
                    }
                }
            }

            System.out.println(sb.toString());
            log.info(sb.toString());
        }

    }
}
