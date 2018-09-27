package com.ume.framework.plugin.sessionman.structure;

/**
 * @author SxL
 * Created on 9/25/2018 1:39 PM.
 */
public class Settings {
    private boolean bControl = false;
    private String strEffective = null;
    private int iUserSessionCount = 0;
    private boolean bSingleAcount = false;
    private int iAllSessionCount = 0;

    public Settings() {
    }

    public Settings(boolean bControl) {
        this.bControl = bControl;
    }

    public Settings(boolean bControl, boolean bSingleAcount) {
        this.bControl = bControl;
        this.bSingleAcount = bSingleAcount;
    }

    public Settings(boolean bControl, boolean bSingleAcount, int iAllSessionCount, int iUserSessionCount) {
        this.bControl = bControl;
        this.bSingleAcount = bSingleAcount;
        this.iAllSessionCount = iAllSessionCount;
        this.iUserSessionCount = iUserSessionCount;
    }

    public boolean isbControl() {
        return this.bControl;
    }

    public String getStrEffective() {
        return this.strEffective;
    }

    public int getiUserSessionCount() {
        return this.iUserSessionCount;
    }

    public int getiAllSessionCount() {
        return this.iAllSessionCount;
    }

    public boolean isbSingleAcount() {
        return this.bSingleAcount;
    }
}
