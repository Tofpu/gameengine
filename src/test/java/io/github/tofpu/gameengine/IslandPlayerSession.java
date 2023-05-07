package io.github.tofpu.gameengine;

import io.tofpu.gameengine.GamePlayerSession;

import java.util.UUID;

public class IslandPlayerSession implements GamePlayerSession {
    private final UUID id;

    public IslandPlayerSession(UUID id) {
        this.id = id;
    }
}
