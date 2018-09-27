package org.jasig.cas.web.view;

import org.jasig.cas.validation.Assertion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author SxL
 * Created on 9/25/2018 6:11 PM.
 */
public final class Cas10ResponseView extends AbstractCasView {
    private boolean successResponse;

    public Cas10ResponseView() {
    }

    @Override
    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Assertion assertion = this.getAssertionFrom(model);
        if (this.successResponse) {
            response.getWriter().print("yes\n" + assertion.getPrimaryAuthentication().getPrincipal().getId() + "\n");
        } else {
            response.getWriter().print("no\n\n");
        }

    }

    public void setSuccessResponse(boolean successResponse) {
        this.successResponse = successResponse;
    }
}
