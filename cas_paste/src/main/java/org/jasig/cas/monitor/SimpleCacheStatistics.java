package org.jasig.cas.monitor;

import org.apache.commons.io.IOUtils;

import java.util.Formatter;

/**
 * @author SxL
 * Created on 9/25/2018 3:02 PM.
 */
public class SimpleCacheStatistics implements CacheStatistics {
    private static final double BYTES_PER_MB = 1048510.0D;
    private final long size;
    private final long capacity;
    private final long evictions;
    private String name;

    public SimpleCacheStatistics(long size, long capacity, long evictions) {
        this.size = size;
        this.capacity = capacity;
        this.evictions = evictions;
    }

    public SimpleCacheStatistics(long size, long capacity, long evictions, String name) {
        this.size = size;
        this.capacity = capacity;
        this.evictions = evictions;
        this.name = name;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public long getCapacity() {
        return this.capacity;
    }

    @Override
    public long getEvictions() {
        return this.evictions;
    }

    @Override
    public int getPercentFree() {
        return this.capacity == 0L ? 0 : (int)((this.capacity - this.size) * 100L / this.capacity);
    }

    @Override
    public void toString(StringBuilder builder) {
        if (this.name != null) {
            builder.append(this.name).append(':');
        }

        Formatter formatter = new Formatter(builder);
        formatter.format("%.2f", (double)this.size / 1048510.0D);
        builder.append("MB used, ");
        builder.append(this.getPercentFree()).append("% free, ");
        builder.append(this.evictions).append(" evictions");
        IOUtils.closeQuietly(formatter);
    }

    @Override
    public String getName() {
        return this.name;
    }
}
