package com.ume.framework.plugin.sessionman.structure;

import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Date;

/**
 * @author SxL
 * Created on 9/25/2018 1:38 PM.
 */
public class SessionData {
    private HttpSession hs = null;
    private String user = null;
    private String ip = null;
    private Date time = new Date();

    public SessionData() {
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public HttpSession getHs() {
        return this.hs;
    }

    public void setHs(HttpSession hs) {
        this.hs = hs;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void clear() {
        this.hs = null;
        this.user = null;
        this.ip = null;
    }

    public void outputAliveTimeAsMinutes() {
        long l = Calendar.getInstance().getTime().getTime() - this.time.getTime();
        System.out.println("___________session瓒呮椂鏃堕棿涓�" + (double)l * 1.0D / 1000.0D + "绉掗挓");
    }

    public String showInfo() {
        return "user:" + this.getUser() + ";  ip:" + this.getIp() + ";   time:" + this.time + ";   ";
    }

    public static void main(String[] args) throws InterruptedException {
        Date d1 = new Date();
        Thread.sleep(10000L);
        Date d2 = new Date();
        long l = d2.getTime() - d1.getTime();
        System.out.println("" + Math.round((double)l * 1.0D / 1000.0D) + "绉掗挓");
    }
}
