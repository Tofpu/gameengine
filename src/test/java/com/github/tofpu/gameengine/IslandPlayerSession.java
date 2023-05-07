package com.github.tofpu.gameengine;

import java.util.UUID;

public class IslandPlayerSession implements GamePlayerSession {
    private final UUID id;

    public IslandPlayerSession(UUID id) {
        this.id = id;
    }
}
