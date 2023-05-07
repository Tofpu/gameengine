package io.github.tofpu.gameengine;

import io.tofpu.gameengine.GameSession;

import java.util.UUID;

public class IslandGameSession implements GameSession {
    private final UUID id;


    public IslandGameSession(UUID id) {
        this.id = id;
    }

    @Override
    public UUID getId() {
        return id;
    }
}
