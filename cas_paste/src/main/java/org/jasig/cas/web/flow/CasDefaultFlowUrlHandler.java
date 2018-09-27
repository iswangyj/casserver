package org.jasig.cas.web.flow;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.webflow.context.servlet.DefaultFlowUrlHandler;
import org.springframework.webflow.core.collection.AttributeMap;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author SxL
 * Created on 9/25/2018 5:40 PM.
 */
public final class CasDefaultFlowUrlHandler extends DefaultFlowUrlHandler {
    public static final String DEFAULT_FLOW_EXECUTION_KEY_PARAMETER = "execution";
    private String flowExecutionKeyParameter = "execution";
    @NotNull
    private JdbcTemplate jdbcTemplate;
    @NotNull
    private DataSource dataSource;

    public CasDefaultFlowUrlHandler() {
    }

    public void setFlowExecutionKeyParameter(String parameterName) {
        this.flowExecutionKeyParameter = parameterName;
    }

    @Override
    public String getFlowExecutionKey(HttpServletRequest request) {
        return request.getParameter(this.flowExecutionKeyParameter);
    }

    @Override
    public String createFlowExecutionUrl(String flowId, String flowExecutionKey, HttpServletRequest request) {
        StringBuffer builder = new StringBuffer();
        builder.append(request.getRequestURI());
        builder.append("?");
        Map<String, Object> flowParams = new LinkedHashMap(request.getParameterMap());
        flowParams.put(this.flowExecutionKeyParameter, flowExecutionKey);
        this.appendQueryParameters(builder, flowParams, this.getEncodingScheme(request));
        return builder.toString();
    }

    @Override
    public String createFlowDefinitionUrl(String flowId, AttributeMap input, HttpServletRequest request) {
        return request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
    }

    public final void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }
}

