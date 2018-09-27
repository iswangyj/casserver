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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author SxL
 * Created on 9/25/2018 5:25 PM.
 */
@Component("casca")
@Controller
@RequestMapping(
        value = {"/casca"},
        method = {RequestMethod.GET}
)
public class CALoginAction extends DelegateController {
    public CALoginAction() {
    }

    @RequestMapping(
            value = {"loginServHello"},
            method = {RequestMethod.POST}
    )
    public ModelAndView loginServHello(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            InputStream in = request.getInputStream();
//            boolean i = true;
            byte[] b = new byte[1024];
            StringBuffer sb = new StringBuffer();

            int i;
            while((i = in.read(b)) != -1) {
                sb.append(new String(b, 0, i));
            }

            String ClientMsg = sb.toString();
            String messageCreat = "";
            IscJcrypt is1 = IscJcrypt.serverHello(ClientMsg);
            int check = is1.ErrCode;
            if (check == 0) {
                messageCreat = is1.strResult;
            } else {
                messageCreat = "err";
            }

            OutputStream os = response.getOutputStream();
            os.write(messageCreat.getBytes());
            os.flush();
            os.close();
        } catch (Exception var12) {
            ;
        }

        return null;
    }

    @RequestMapping(
            value = {"loginServAuth"},
            method = {RequestMethod.POST}
    )
    public ModelAndView loginServAuth(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            SAXReader reader = new SAXReader();
            InputStream in = request.getInputStream();
            Document doc = reader.read(in);
            String str_clientAuth = doc.selectSingleNode("//RLT/ClientAuth").getText();
            boolean check = false;
            String name1 = "";
            String messageCreat = "";
            IscJcrypt is3 = IscJcrypt.ParseCert(str_clientAuth, "e", 0);
            IscJcrypt is1 = IscJcrypt.ParseCert(str_clientAuth, "uid", 0);
            if (!check) {
                name1 = is3.strResult;
                messageCreat = name1;
            } else {
                messageCreat = "err";
            }

            OutputStream os = response.getOutputStream();
            os.write(messageCreat.getBytes());
            os.flush();
            os.close();
            return null;
        } catch (Exception var13) {
            var13.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean canHandle(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return null;
    }
}

