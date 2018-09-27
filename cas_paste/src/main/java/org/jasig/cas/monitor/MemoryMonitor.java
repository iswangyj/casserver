package org.jasig.cas.monitor;

/**
 * @author SxL
 * Created on 9/25/2018 3:00 PM.
 */
public class MemoryMonitor implements Monitor<MemoryStatus> {
    public static final int DEFAULT_FREE_MEMORY_WARN_THRESHOLD = 10;
    private long freeMemoryWarnThreshold = 10L;

    public MemoryMonitor() {
    }

    public void setFreeMemoryWarnThreshold(long threshold) {
        if (threshold < 0L) {
            throw new IllegalArgumentException("Warning threshold must be non-negative.");
        } else {
            this.freeMemoryWarnThreshold = threshold;
        }
    }

    public String getName() {
        return MemoryMonitor.class.getSimpleName();
    }

    public MemoryStatus observe() {
        long free = Runtime.getRuntime().freeMemory();
        long total = Runtime.getRuntime().totalMemory();
        StatusCode code;
        if (free * 100L / total < this.freeMemoryWarnThreshold) {
            code = StatusCode.WARN;
        } else {
            code = StatusCode.OK;
        }

        return new MemoryStatus(code, free, total);
    }
}