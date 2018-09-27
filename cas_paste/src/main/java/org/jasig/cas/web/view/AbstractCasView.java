package org.jasig.cas.web.view;

import org.jasig.cas.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.AbstractView;

import java.util.Map;

/**
 * @author SxL
 * Created on 9/25/2018 6:10 PM.
 */
public abstract class AbstractCasView extends AbstractView {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public AbstractCasView() {
    }

    protected final Assertion getAssertionFrom(Map<String, Object> model) {
        return (Assertion)model.get("assertion");
    }
}

