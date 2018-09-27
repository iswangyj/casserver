package org.jasig.cas.util;

import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Map;

/**
 * @author SxL
 * Created on 9/25/2018 5:08 PM.
 */
public final class AutowiringSchedulerFactoryBean extends SchedulerFactoryBean implements ApplicationContextAware, InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private ApplicationContext applicationContext;

    public AutowiringSchedulerFactoryBean() {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Trigger> triggers = this.applicationContext.getBeansOfType(Trigger.class);
        super.setTriggers((Trigger[])triggers.values().toArray(new Trigger[triggers.size()]));
        this.logger.debug("Autowired the following triggers defined in application context: {}", triggers.keySet().toString());
        super.afterPropertiesSet();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        super.setApplicationContext(applicationContext);
        this.applicationContext = applicationContext;
    }
}
