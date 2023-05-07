package com.github.tofpu.gameengine;

import java.util.UUID;

public class IslandGameSession implements GameSession {
    private final UUID id;
    private final int islandSlot;


    public IslandGameSession(UUID id, int islandSlot) {
        this.id = id;
        this.islandSlot = islandSlot;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public int getIslandSlot() {
        return islandSlot;
    }
}
