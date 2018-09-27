package org.jasig.cas.web;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Iterator;
import java.util.List;

/**
 * @author SxL
 * Created on 9/25/2018 5:20 PM.
 */
public class DelegatingController extends AbstractController {
    private List<DelegateController> delegates;
    private static final String DEFAULT_ERROR_VIEW_NAME = "casServiceFailureView";
    @NotNull
    private String failureView = "casServiceFailureView";

    public DelegatingController() {
    }

    @Override
    protected final ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Iterator var3 = this.delegates.iterator();

        DelegateController delegate;
        do {
            if (!var3.hasNext()) {
                return this.generateErrorView("INVALID_REQUEST", "INVALID_REQUEST", (Object[])null);
            }

            delegate = (DelegateController)var3.next();
        } while(!delegate.canHandle(request, response));

        return delegate.handleRequest(request, response);
    }

    private ModelAndView generateErrorView(String code, String description, Object[] args) {
        ModelAndView modelAndView = new ModelAndView(this.failureView);
        String convertedDescription = this.getMessageSourceAccessor().getMessage(description, args, description);
        modelAndView.addObject("code", code);
        modelAndView.addObject("description", convertedDescription);
        return modelAndView;
    }

    @NotNull
    public void setDelegates(List<DelegateController> delegates) {
        this.delegates = delegates;
    }

    public final void setFailureView(String failureView) {
        this.failureView = failureView;
    }
}
