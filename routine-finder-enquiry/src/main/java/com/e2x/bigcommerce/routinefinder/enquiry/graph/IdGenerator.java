package com.e2x.bigcommerce.routinefinder.enquiry.graph;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {
    private static final AtomicLong CURRENT_ID = new AtomicLong(0);

    public static long nextLong() {
        return CURRENT_ID.incrementAndGet();
    }
}
