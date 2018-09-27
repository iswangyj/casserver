package org.jasig.cas.util;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import java.util.Locale;

/**
 * @author SxL
 * Created on 9/25/2018 5:17 PM.
 */
public final class SpringAwareMessageMessageInterpolator implements MessageInterpolator, MessageSourceAware {
    private MessageInterpolator defaultMessageInterpolator = Validation.byDefaultProvider().configure().getDefaultMessageInterpolator();
    private MessageSource messageSource;

    public SpringAwareMessageMessageInterpolator() {
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String interpolate(String s, Context context) {
        return this.interpolate(s, context, LocaleContextHolder.getLocale());
    }

    public String interpolate(String s, Context context, Locale locale) {
        try {
            return this.messageSource.getMessage(s, context.getConstraintDescriptor().getAttributes().values().toArray(new Object[context.getConstraintDescriptor().getAttributes().size()]), locale);
        } catch (NoSuchMessageException var5) {
            return this.defaultMessageInterpolator.interpolate(s, context, locale);
        }
    }
}

