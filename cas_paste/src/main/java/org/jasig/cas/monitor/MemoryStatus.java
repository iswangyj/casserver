package org.jasig.cas.monitor;

/**
 * @author SxL
 * Created on 9/25/2018 3:00 PM.
 */
public class MemoryStatus extends Status {
    private static final double BYTES_PER_MB = 1048510.0D;
    private final long freeMemory;
    private final long totalMemory;

    public MemoryStatus(StatusCode code, long free, long total) {
        super(code, String.format("%.2fMB free, %.2fMB total.", (double)free / 1048510.0D, (double)total / 1048510.0D));
        this.freeMemory = free;
        this.totalMemory = total;
    }

    public long getFreeMemory() {
        return this.freeMemory;
    }

    public long getTotalMemory() {
        return this.totalMemory;
    }
}
