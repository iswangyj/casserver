package org.jasig.cas.authentication.principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author SxL
 * Created on 9/25/2018 2:47 PM.
 */
public final class Response {
    private static final Pattern NON_PRINTABLE = Pattern.compile("[\\x00-\\x1F\\x7F]+");
    protected static final Logger LOGGER = LoggerFactory.getLogger(Response.class);
    private final Response.ResponseType responseType;
    private final String url;
    private final Map<String, String> attributes;

    protected Response(Response.ResponseType responseType, String url, Map<String, String> attributes) {
        this.responseType = responseType;
        this.url = url;
        this.attributes = attributes;
    }

    public static Response getPostResponse(String url, Map<String, String> attributes) {
        return new Response(Response.ResponseType.POST, url, attributes);
    }

    public static Response getRedirectResponse(String url, Map<String, String> parameters) {
        StringBuilder builder = new StringBuilder(parameters.size() * 40 + 100);
        boolean isFirst = true;
        String[] fragmentSplit = sanitizeUrl(url).split("#");
        builder.append(fragmentSplit[0]);
        Iterator var5 = parameters.entrySet().iterator();

        while(var5.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var5.next();
            if (entry.getValue() != null) {
                if (isFirst) {
                    builder.append(url.contains("?") ? "&" : "?");
                    isFirst = false;
                } else {
                    builder.append("&");
                }

                builder.append((String)entry.getKey());
                builder.append("=");

                try {
                    builder.append(URLEncoder.encode((String)entry.getValue(), "UTF-8"));
                } catch (Exception var8) {
                    builder.append((String)entry.getValue());
                }
            }
        }

        if (fragmentSplit.length > 1) {
            builder.append("#");
            builder.append(fragmentSplit[1]);
        }

        return new Response(Response.ResponseType.REDIRECT, builder.toString(), parameters);
    }

    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    public Response.ResponseType getResponseType() {
        return this.responseType;
    }

    public String getUrl() {
        return this.url;
    }

    private static String sanitizeUrl(String url) {
        Matcher m = NON_PRINTABLE.matcher(url);
        StringBuffer sb = new StringBuffer(url.length());

        boolean hasNonPrintable;
        for(hasNonPrintable = false; m.find(); hasNonPrintable = true) {
            m.appendReplacement(sb, " ");
        }

        m.appendTail(sb);
        if (hasNonPrintable) {
            LOGGER.warn("The following redirect URL has been sanitized and may be sign of attack:\n{}", url);
        }

        return sb.toString();
    }

    public static enum ResponseType {
        POST,
        REDIRECT;

        private ResponseType() {
        }
    }
}
