package com.github.tofpu.gameengine.island;

public class DemoIsland implements Island {
    private final int slot;
    private final int width;

    public DemoIsland(int slot, int width) {
        this.slot = slot;
        this.width = width;
    }

    public static Island create(int slot, int width) {
        return new DemoIsland(slot, width);
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public int getWidth() {
        return width;
    }
}
