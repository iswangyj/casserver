package org.jasig.cas.web.check;

import org.jasig.cas.web.DelegateController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author SxL
 * Created on 9/25/2018 5:35 PM.
 */
public class Cas4CheckServerSurvival extends DelegateController {
    public Cas4CheckServerSurvival() {
    }

    @Override
    public boolean canHandle(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServletOutputStream os = response.getOutputStream();

        Object var5;
        try {
            os.write("UME_CAS".getBytes());
            os.flush();
            Object var4 = null;
            return (ModelAndView)var4;
        } catch (Exception var9) {
            os.write("error".getBytes());
            os.flush();
            var5 = null;
        } finally {
            if (null != os) {
                os.close();
                os = null;
            }

        }

        return (ModelAndView)var5;
    }
}
