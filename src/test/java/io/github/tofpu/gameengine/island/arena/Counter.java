package io.github.tofpu.gameengine.island.arena;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Counter {
    private final AtomicInteger integer = new AtomicInteger();

    public int incrementBy(int... ints) {
        return integer.getAndAdd(Arrays.stream(ints).sum());
    }
}
