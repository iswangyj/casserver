package org.jasig.cas.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author SxL
 * Created on 9/25/2018 5:10 PM.
 */
public final class DefaultLongNumericGenerator implements LongNumericGenerator {
    private static final int MAX_STRING_LENGTH = Long.toString(9223372036854775807L).length();
    private static final int MIN_STRING_LENGTH = 1;
    private final AtomicLong count;

    public DefaultLongNumericGenerator() {
        this(0L);
    }

    public DefaultLongNumericGenerator(long initialValue) {
        this.count = new AtomicLong(initialValue);
    }

    @Override
    public long getNextLong() {
        return this.getNextValue();
    }

    @Override
    public String getNextNumberAsString() {
        return Long.toString(this.getNextValue());
    }

    @Override
    public int maxLength() {
        return MAX_STRING_LENGTH;
    }

    @Override
    public int minLength() {
        return 1;
    }

    protected long getNextValue() {
        return this.count.compareAndSet(9223372036854775807L, 0L) ? 9223372036854775807L : this.count.getAndIncrement();
    }
}
