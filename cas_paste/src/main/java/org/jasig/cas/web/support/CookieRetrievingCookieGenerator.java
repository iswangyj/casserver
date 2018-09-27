package org.jasig.cas.web.support;

import org.springframework.util.StringUtils;
import org.springframework.web.util.CookieGenerator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author SxL
 * Created on 9/25/2018 5:55 PM.
 */
public class CookieRetrievingCookieGenerator extends CookieGenerator {
    private int rememberMeMaxAge = 7889231;

    public CookieRetrievingCookieGenerator() {
    }

    public void addCookie(HttpServletRequest request, HttpServletResponse response, String cookieValue) {
        if (!StringUtils.hasText(request.getParameter("rememberMe"))) {
            super.addCookie(response, cookieValue);
        } else {
            Cookie cookie = this.createCookie(cookieValue);
            cookie.setMaxAge(this.rememberMeMaxAge);
            if (this.isCookieSecure()) {
                cookie.setSecure(true);
            }

            response.addCookie(cookie);
        }

    }

    @Override
    public void removeCookie(HttpServletResponse response) {
        Cookie cookie = this.createCookie("");
        cookie.setMaxAge(0);
        super.addCookie(response, "");
    }

    public String retrieveCookieValue(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, this.getCookieName());
        return cookie == null ? null : cookie.getValue();
    }

    public void setRememberMeMaxAge(int maxAge) {
        this.rememberMeMaxAge = maxAge;
    }
}

