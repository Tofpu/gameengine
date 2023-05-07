package com.github.tofpu.gameengine;

import com.github.tofpu.gameengine.util.ProgramCorrectnessHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class GameEngine {

    public static GameEngine create(BiFunction<UUID, Object[], GamePlayerSession> playerSessionFunction, BiFunction<UUID, Object[], GameSession> gameSessionFunction, BiConsumer<GameSession, GamePlayerSession> onStartConsumer, BiConsumer<GameSession, GamePlayerSession> onEndConsumer) {
        return new GameEngine(playerSessionFunction, gameSessionFunction, onStartConsumer, onEndConsumer);
    }

    private final BiFunction<UUID, Object[], GamePlayerSession> playerSessionFunction;
    private final BiFunction<UUID, Object[], GameSession> gameSessionFunction;
    private final BiConsumer<GameSession, GamePlayerSession> onStartConsumer;

    private final BiConsumer<GameSession, GamePlayerSession> onEndConsumer;

    private final Map<UUID, GameInfo> gameInfoMap = new HashMap<>();

    public GameEngine(BiFunction<UUID, Object[], GamePlayerSession> playerSessionFunction, BiFunction<UUID, Object[], GameSession> gameSessionFunction, BiConsumer<GameSession, GamePlayerSession> onStartConsumer, BiConsumer<GameSession, GamePlayerSession> onEndConsumer) {
        this.playerSessionFunction = playerSessionFunction;
        this.gameSessionFunction = gameSessionFunction;
        this.onStartConsumer = onStartConsumer;
        this.onEndConsumer = onEndConsumer;
    }

    public GameSession begin(UUID playerId, Object... data) {
        ProgramCorrectnessHelper.requireState(!isPlaying(playerId), "Player %s is already in a game", playerId);

        GameSession gameSession = gameSessionFunction.apply(playerId, data);
        GamePlayerSession playerSession = playerSessionFunction.apply(playerId, data);

        onStartConsumer.accept(gameSession, playerSession);

        this.gameInfoMap.put(playerId, new GameInfo(gameSession, playerSession));
        return gameSession;
    }

    public boolean isPlaying(UUID playerId) {
        return this.gameInfoMap.containsKey(playerId);
    }

    public GameSession end(UUID playerId) {
        ProgramCorrectnessHelper.requireState(isPlaying(playerId), "Player %s is not in a game", playerId);

        GameInfo gameInfo = gameInfoMap.remove(playerId);
        onEndConsumer.accept(gameInfo.gameSession, gameInfo.gamePlayerSession);
        return gameInfo.gameSession;
    }

    protected record GameInfo(GameSession gameSession, GamePlayerSession gamePlayerSession) {}
}
