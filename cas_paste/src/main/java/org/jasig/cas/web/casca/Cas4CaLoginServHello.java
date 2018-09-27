package org.jasig.cas.web.casca;

import auth.IscJcrypt;
import org.jasig.cas.web.DelegateController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author SxL
 * Created on 9/25/2018 5:33 PM.
 */
public class Cas4CaLoginServHello extends DelegateController {
    public Cas4CaLoginServHello() {
    }

    @Override
    public boolean canHandle(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        OutputStream os = response.getOutputStream();
        String messageCreat = "";

        Object var6;
        try {
            InputStream in = request.getInputStream();
//            int i = true;
            byte[] b = new byte[1024];
            StringBuffer sb = new StringBuffer();

            int i;
            while((i = in.read(b)) != -1) {
                sb.append(new String(b, 0, i));
            }

            String ClientMsg = sb.toString();
            IscJcrypt is1 = IscJcrypt.serverHello(ClientMsg);
            int check = is1.ErrCode;
            if (check == 0) {
                messageCreat = is1.strResult;
            } else {
                messageCreat = "err";
            }

            os.write(messageCreat.getBytes());
            os.flush();
            return null;
        } catch (Exception var15) {
            var15.printStackTrace();
            messageCreat = "exception";
            os.write(messageCreat.getBytes());
            os.flush();
            var6 = null;
        } finally {
            if (null != os) {
                os.close();
                os = null;
            }

        }

        return (ModelAndView)var6;
    }
}
