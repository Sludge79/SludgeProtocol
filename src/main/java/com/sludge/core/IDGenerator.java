package com.sludge.core;

import java.util.concurrent.atomic.AtomicInteger;

public class IDGenerator {
    private static final AtomicInteger ID = new AtomicInteger(0);

    public static int get(){
        return ID.getAndIncrement();
    }
}
