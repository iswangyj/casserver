package org.jasig.cas.services.ume;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SxL
 * Created on 9/25/2018 3:26 PM.
 */
public class CasServerCfgPub {
    private static final Logger LOGGER = LoggerFactory.getLogger(CasServerCfgPub.class);
    private static String portalPage = null;
    private static boolean mode = true;
    private static Map<String, String> mapProperties = new HashMap();
    public static final String TYPE_PORTAL_PAGE = "portalPage";
    public static final String TYPE_MODE = "mode";
    public static final String CA_CHECK_SERVER_IP = "CA_CHECK_SERVER_IP";

    private CasServerCfgPub() {
    }

    public static void setCasServerCfg(ServletContext context) {
        if (null == portalPage) {
            init(context);
        }

    }

    private static synchronized void init(ServletContext context) {
        if (null == portalPage || "".equals(portalPage)) {
            InputStream is = context.getResourceAsStream("/WEB-INF/conf/casInterface.xml");

            try {
                String cfg = FileUtil.getString4InputStream(is, "UTF-8");
                Document doc = DocumentHelper.parseText(cfg);
                Element eleRoot = doc.getRootElement();
                if (null == eleRoot) {
                    throw new Exception("配置文件（casInterface.xml）不正确！");
                }

                Element eleInterfacePortalPage = (Element)eleRoot.selectSingleNode("interface[@type = 'portalPage']");
                Element eleMode;
                if (null != eleInterfacePortalPage) {
                    eleMode = (Element)eleInterfacePortalPage.selectSingleNode("portalPage");
                    if (null != eleMode && !"".equals(eleMode.getText())) {
                        portalPage = eleMode.getText();
                    }
                }

                eleMode = (Element)eleRoot.selectSingleNode("interface[@type = 'mode']");
                if (null != eleMode && !"".equals(eleMode.attributeValue("value"))) {
                    String modeValue = eleMode.attributeValue("value");
                    if ("casassis".equals(modeValue)) {
                        mode = false;
                    }
                }

                Element eleProps = (Element)eleRoot.selectSingleNode("properties");
                if (null != eleProps) {
                    List<Element> lstProps = eleProps.selectNodes("property");
                    if (null != lstProps && lstProps.size() > 0) {
                        for(int i = 0; i < lstProps.size(); ++i) {
                            Element eleProp = (Element)lstProps.get(i);
                            if (null != eleProp) {
                                String key = eleProp.attributeValue("key");
                                String value = eleProp.attributeValue("value");
                                if (null != key && !"".equals(key) && null != value && !"".equals(value)) {
                                    mapProperties.put(key, value);
                                }
                            }
                        }
                    }
                }
            } catch (DocumentException var23) {
                LOGGER.error(var23.getMessage());
            } catch (Exception var24) {
                LOGGER.error(var24.getMessage());
            } finally {
                try {
                    if (null != is) {
                        is.close();
                    }
                } catch (Exception var22) {
                    var22.printStackTrace();
                }

            }

        }
    }

    public static String getProperty(String key) {
        return (String)mapProperties.get(key);
    }

    public static String getPortalPage() {
        return portalPage;
    }

    public static boolean getMode() {
        return mode;
    }

    public static String getServicePath(String url) {
        String service = null;
        int idx = 0;

        for(int i = 0; i < 4; ++i) {
            idx = url.indexOf("/", idx + 1);
            if (idx == -1) {
                idx = url.length();
            }
        }

        service = url.substring(0, idx);
        if (service.lastIndexOf(":") < 6) {
            idx = 0;

            for(int i = 0; i < 3; ++i) {
                idx = url.indexOf("/", idx + 1);
                if (idx == -1) {
                    idx = url.length();
                }
            }

            service = url.substring(0, idx);
        }

        if (service.endsWith("/")) {
            service = service.substring(0, service.length() - 1);
        }

        return service;
    }

    public static void main(String[] args) {
        String urlComp = "http://141.3.119.41";
        String url = "http://141.3.119.41:80/asda/sdf";
        System.out.println(getServicePath(url));
    }
}
