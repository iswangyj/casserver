package org.jasig.cas.web.view;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

/**
 * @author SxL
 * Created on 9/25/2018 6:11 PM.
 */
public class CasReloadableMessageBundle extends ReloadableResourceBundleMessageSource {
    private String[] basenames;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CasReloadableMessageBundle() {
    }

    @Override
    protected String getDefaultMessage(String code) {
        String messageToReturn = super.getDefaultMessage(code);
        if (!StringUtils.isBlank(messageToReturn) && messageToReturn.equals(code)) {
            this.logger.warn("The code [{}] cannot be found in the default language bundle and will be used as the message itself.", code);
        }

        return messageToReturn;
    }

    @Override
    protected String getMessageInternal(String code, Object[] args, Locale locale) {
        boolean foundCode = false;
        if (!locale.equals(Locale.ENGLISH)) {
            for(int i = 0; !foundCode && i < this.basenames.length; ++i) {
                String filename = this.basenames[i] + "_" + locale;
                this.logger.debug("Examining language bundle [{}] for the code [{}]", filename, code);
                PropertiesHolder holder = this.getProperties(filename);
                foundCode = holder != null && holder.getProperties() != null && holder.getProperty(code) != null;
            }

            if (!foundCode) {
                this.logger.warn("The code [{}] cannot be found in the language bundle for the locale [{}]", code, locale);
            }
        }

        return super.getMessageInternal(code, args, locale);
    }

    @Override
    public void setBasenames(String... basenames) {
        this.basenames = basenames;
        super.setBasenames(basenames);
    }
}
