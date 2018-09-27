package org.jasig.cas.web.support;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author SxL
 * Created on 9/25/2018 5:53 PM.
 */
public abstract class AbstractInMemoryThrottledSubmissionHandlerInterceptorAdapter extends AbstractThrottledSubmissionHandlerInterceptorAdapter {
    private final ConcurrentMap<String, Date> ipMap = new ConcurrentHashMap();

    public AbstractInMemoryThrottledSubmissionHandlerInterceptorAdapter() {
    }

    @Override
    protected final boolean exceedsThreshold(HttpServletRequest request) {
        Date last = (Date)this.ipMap.get(this.constructKey(request));
        if (last == null) {
            return false;
        } else {
            return this.submissionRate(new Date(), last) > this.getThresholdRate();
        }
    }

    @Override
    protected final void recordSubmissionFailure(HttpServletRequest request) {
        this.ipMap.put(this.constructKey(request), new Date());
    }

    protected abstract String constructKey(HttpServletRequest var1);

    public final void decrementCounts() {
        Set<String> keys = this.ipMap.keySet();
        this.logger.debug("Decrementing counts for throttler.  Starting key count: {}", keys.size());
        Date now = new Date();
        Iterator iter = keys.iterator();

        while(iter.hasNext()) {
            String key = (String)iter.next();
            if (this.submissionRate(now, (Date)this.ipMap.get(key)) < this.getThresholdRate()) {
                this.logger.trace("Removing entry for key {}", key);
                iter.remove();
            }
        }

        this.logger.debug("Done decrementing count for throttler.");
    }

    private double submissionRate(Date a, Date b) {
        return 1000.0D / (double)(a.getTime() - b.getTime());
    }
}

