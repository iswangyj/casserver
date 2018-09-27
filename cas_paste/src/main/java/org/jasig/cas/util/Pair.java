package org.jasig.cas.util;

/**
 * @author SxL
 * Created on 9/25/2018 5:12 PM.
 */
public class Pair<A, B> {
    private final A first;
    private final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public final A getFirst() {
        return this.first;
    }

    public final B getSecond() {
        return this.second;
    }
}

