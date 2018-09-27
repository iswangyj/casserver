package org.jasig.cas.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author SxL
 * Created on 9/25/2018 5:20 PM.
 */
public abstract class DelegateController extends AbstractController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public DelegateController() {
    }

    public abstract boolean canHandle(HttpServletRequest var1, HttpServletResponse var2);
}

