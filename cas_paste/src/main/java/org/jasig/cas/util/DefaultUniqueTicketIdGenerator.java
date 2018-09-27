package org.jasig.cas.util;

/**
 * @author SxL
 * Created on 9/25/2018 5:11 PM.
 */
public final class DefaultUniqueTicketIdGenerator implements UniqueTicketIdGenerator {
    private final NumericGenerator numericGenerator;
    private final RandomStringGenerator randomStringGenerator;
    private final String suffix;

    public DefaultUniqueTicketIdGenerator() {
        this((String)null);
    }

    public DefaultUniqueTicketIdGenerator(int maxLength) {
        this(maxLength, (String)null);
    }

    public DefaultUniqueTicketIdGenerator(String suffix) {
        this.numericGenerator = new DefaultLongNumericGenerator(1L);
        this.randomStringGenerator = new DefaultRandomStringGenerator();
        if (suffix != null) {
            this.suffix = "-" + suffix;
        } else {
            this.suffix = null;
        }

    }

    public DefaultUniqueTicketIdGenerator(int maxLength, String suffix) {
        this.numericGenerator = new DefaultLongNumericGenerator(1L);
        this.randomStringGenerator = new DefaultRandomStringGenerator(maxLength);
        if (suffix != null) {
            this.suffix = "-" + suffix;
        } else {
            this.suffix = null;
        }

    }

    @Override
    public String getNewTicketId(String prefix) {
        String number = this.numericGenerator.getNextNumberAsString();
        StringBuilder buffer = new StringBuilder(prefix.length() + 2 + (this.suffix != null ? this.suffix.length() : 0) + this.randomStringGenerator.getMaxLength() + number.length());
        buffer.append(prefix);
        buffer.append("-");
        buffer.append(number);
        buffer.append("-");
        buffer.append(this.randomStringGenerator.getNewString());
        if (this.suffix != null) {
            buffer.append(this.suffix);
        }

        return buffer.toString();
    }
}
