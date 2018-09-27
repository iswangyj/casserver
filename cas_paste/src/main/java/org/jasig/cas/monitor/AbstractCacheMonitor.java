package org.jasig.cas.monitor;

/**
 * @author SxL
 * Created on 9/25/2018 2:56 PM.
 */
public abstract class AbstractCacheMonitor extends AbstractNamedMonitor<CacheStatus> {
    public static final int DEFAULT_WARN_FREE_THRESHOLD = 10;
    public static final long DEFAULT_EVICTION_THRESHOLD = 0L;
    private int warnFreeThreshold = 10;
    private long evictionThreshold = 0L;

    public AbstractCacheMonitor() {
    }

    public void setWarnFreeThreshold(int percent) {
        this.warnFreeThreshold = percent;
    }

    public void setEvictionThreshold(long count) {
        this.evictionThreshold = count;
    }

    @Override
    public CacheStatus observe() {
        CacheStatus status;
        try {
            CacheStatistics[] statistics = this.getStatistics();
            if (statistics == null || statistics.length == 0) {
                return new CacheStatus(StatusCode.ERROR, "Cache statistics not available.", new CacheStatistics[0]);
            }

            StatusCode overall = StatusCode.OK;
            CacheStatistics[] var5 = statistics;
            int var6 = statistics.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                CacheStatistics stats = var5[var7];
                StatusCode code = this.status(stats);
                if (code.value() > overall.value()) {
                    overall = code;
                }
            }

            status = new CacheStatus(overall, (String)null, statistics);
        } catch (Exception var9) {
            status = new CacheStatus(var9);
        }

        return status;
    }

    protected abstract CacheStatistics[] getStatistics();

    protected StatusCode status(CacheStatistics statistics) {
        StatusCode code;
        if (statistics.getEvictions() > this.evictionThreshold) {
            code = StatusCode.WARN;
        } else if (statistics.getPercentFree() < this.warnFreeThreshold) {
            code = StatusCode.WARN;
        } else {
            code = StatusCode.OK;
        }

        return code;
    }
}
