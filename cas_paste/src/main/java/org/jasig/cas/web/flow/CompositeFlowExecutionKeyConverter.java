package org.jasig.cas.web.flow;

import org.springframework.binding.convert.converters.Converter;
import org.springframework.webflow.execution.repository.support.CompositeFlowExecutionKey;

/**
 * @author SxL
 * Created on 9/25/2018 5:42 PM.
 */
public final class CompositeFlowExecutionKeyConverter implements Converter {
    public CompositeFlowExecutionKeyConverter() {
    }

    @Override
    public Class getSourceClass() {
        return CompositeFlowExecutionKey.class;
    }

    @Override
    public Class getTargetClass() {
        return String.class;
    }

    @Override
    public Object convertSourceToTargetClass(Object source, Class targetClass) throws Exception {
        return source.toString();
    }
}

