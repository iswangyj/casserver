package org.jasig.cas.util;

import org.springframework.validation.beanvalidation.BeanValidationPostProcessor;

import javax.validation.*;
import java.lang.annotation.ElementType;

/**
 * @author SxL
 * Created on 9/25/2018 5:09 PM.
 */
public final class CustomBeanValidationPostProcessor extends BeanValidationPostProcessor {
    public CustomBeanValidationPostProcessor() {
        Configuration<?> configuration = Validation.byDefaultProvider().configure();
        configuration.traversableResolver(new TraversableResolver() {
            @Override
            public boolean isReachable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType) {
                return true;
            }

            @Override
            public boolean isCascadable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType) {
                return true;
            }
        });
        Validator validator = configuration.buildValidatorFactory().getValidator();
        this.setValidator(validator);
    }
}

