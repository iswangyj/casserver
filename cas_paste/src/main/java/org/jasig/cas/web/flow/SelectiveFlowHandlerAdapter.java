package org.jasig.cas.web.flow;

import org.springframework.util.Assert;
import org.springframework.webflow.mvc.servlet.FlowHandler;
import org.springframework.webflow.mvc.servlet.FlowHandlerAdapter;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

/**
 * @author SxL
 * Created on 9/25/2018 5:46 PM.
 */
public class SelectiveFlowHandlerAdapter extends FlowHandlerAdapter {
    @NotNull
    private List<String> supportedFlowIds;

    public SelectiveFlowHandlerAdapter() {
    }

    public void setSupportedFlowIds(List<String> flowIdList) {
        this.supportedFlowIds = flowIdList;
    }

    public void setSupportedFlowId(String flowId) {
        this.supportedFlowIds = Collections.singletonList(flowId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        Assert.isTrue(!this.supportedFlowIds.isEmpty(), "Must specify at least one supported flow ID");
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof FlowHandler && this.supportedFlowIds.contains(((FlowHandler)handler).getFlowId());
    }
}
