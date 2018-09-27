package org.jasig.cas.util;

import java.security.SecureRandom;

/**
 * @author SxL
 * Created on 9/25/2018 5:10 PM.
 */
public final class DefaultRandomStringGenerator implements RandomStringGenerator {
    private static final char[] PRINTABLE_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ012345679".toCharArray();
    private static final int DEFAULT_MAX_RANDOM_LENGTH = 35;
    private SecureRandom randomizer = new SecureRandom();
    private final int maximumRandomLength;

    public DefaultRandomStringGenerator() {
        this.maximumRandomLength = 35;
    }

    public DefaultRandomStringGenerator(int maxRandomLength) {
        this.maximumRandomLength = maxRandomLength;
    }

    @Override
    public int getMinLength() {
        return this.maximumRandomLength;
    }

    @Override
    public int getMaxLength() {
        return this.maximumRandomLength;
    }

    @Override
    public String getNewString() {
        byte[] random = this.getNewStringAsBytes();
        return this.convertBytesToString(random);
    }

    @Override
    public byte[] getNewStringAsBytes() {
        byte[] random = new byte[this.maximumRandomLength];
        this.randomizer.nextBytes(random);
        return random;
    }

    private String convertBytesToString(byte[] random) {
        char[] output = new char[random.length];

        for(int i = 0; i < random.length; ++i) {
            int index = Math.abs(random[i] % PRINTABLE_CHARACTERS.length);
            output[i] = PRINTABLE_CHARACTERS[index];
        }

        return new String(output);
    }
}
