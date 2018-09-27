package com.ume;

import org.jasig.cas.validation.Assertion;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author SxL
 * Created on 9/25/2018 1:20 PM.
 */
public class TestUtil {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static int iOrder = 0;
    private static int iInfoOrder = 10000;

    public TestUtil() {
    }

    public static void showServerAssertionInfo(Assertion assertion, Object currentClass) {
        String className = currentClass.getClass().getName().toString();
        if (null != assertion) {
            showServerAssertion1(assertion, className);
        } else {
            printInfo("assertion 涓虹┖锛�", className);
        }

    }

    private static void showServerAssertion1(Assertion assertion, String className) {
        String msg = "tService : " + assertion.getService();
        printInfo(msg, className);
    }

    public static void showSessionInfo(HttpSession session, Object currentClass) {
        if (null != session && null != currentClass) {
            String className = currentClass.getClass().getName().toString();
            String sessionID = session.getId();
            printInfo(sessionID, className);
        }
    }

    public static void printExecuteTime(Object currentClass) {
        String className = currentClass.getClass().getName().toString();
        if (iOrder >= 10000) {
            iOrder = 0;
        }

        printInfo(String.valueOf(++iOrder), className);
    }

    public static void printInfo(String msg, Object currentClass) {
        String className = currentClass.getClass().getName().toString();
        printInfo(msg, className);
    }

    private static void printInfo(String msg, String className) {
        String time = sdf.format(Calendar.getInstance().getTime()) + "\t";
        if (iInfoOrder >= 20000) {
            iInfoOrder = 10000;
        }

        if (null != msg && !"".equals(msg)) {
            System.out.println("\t---" + ++iInfoOrder + "---TestUtil : " + time + className + "\t" + msg);
        } else {
            System.out.println("\t---" + ++iInfoOrder + "---\t" + time + className);
        }

    }
}
