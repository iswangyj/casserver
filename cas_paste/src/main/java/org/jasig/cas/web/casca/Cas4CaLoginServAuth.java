package org.jasig.cas.web.casca;

import auth.IscJcrypt;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.jasig.cas.web.DelegateController;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @author SxL
 * Created on 9/25/2018 5:27 PM.
 */
@Component("casca")
@Controller
@RequestMapping(
        value = {"/casca"},
        method = {RequestMethod.GET}
)
public class Cas4CaLoginServAuth extends DelegateController {
    public Cas4CaLoginServAuth() {
    }

    @Override
    public boolean canHandle(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        OutputStream os = response.getOutputStream();
        String messageCreat = "";

        ServletInputStream in;
        try {
            SAXReader reader = new SAXReader();
            in = request.getInputStream();
            Document doc = reader.read(in);
            String str_clientAuth = doc.selectSingleNode("//RLT/ClientAuth").getText();
            String str_ServerHello = doc.selectSingleNode("//RLT/ServerHello").getText();
            IscJcrypt is2 = IscJcrypt.serverAuth(str_clientAuth, str_ServerHello, "192.168.0.97");
            int check = is2.ErrCode;
            String name = "";
            String name1 = "";
            IscJcrypt is3;
            if (check != 0) {
                messageCreat = "err";
                os.write(messageCreat.getBytes());
                os.flush();
                is3 = null;
                return null;
            }

            name = is2.strResult;
            messageCreat = "pass";
            is3 = IscJcrypt.ParseCert(str_clientAuth, "e", 0);
            if (check == 0) {
                name1 = is3.strResult;
                return null;
            }

            messageCreat = "err";
            os.write(messageCreat.getBytes());
            os.flush();
            Object var15 = null;
            return (ModelAndView)var15;
        } catch (Exception var19) {
            var19.printStackTrace();
            messageCreat = "exception";
            os.write(messageCreat.getBytes());
            os.flush();
            in = null;
        } finally {
            if (null != os) {
                os.close();
                os = null;
            }

        }

        return null;
    }
}
