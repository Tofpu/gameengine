package io.tofpu.gameengine;

import io.tofpu.gameengine.util.ProgramCorrectnessHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class GameEngine {

    public static GameEngine create(Function<UUID, GamePlayerSession> playerSessionFunction, Function<UUID, GameSession> gameSessionFunction, BiConsumer<GameSession, GamePlayerSession> onStartConsumer, BiConsumer<GameSession, GamePlayerSession> onEndConsumer) {
        return new GameEngine(playerSessionFunction, gameSessionFunction, onStartConsumer, onEndConsumer);
    }

    private final Function<UUID, GamePlayerSession> playerSessionFunction;
    private final Function<UUID, GameSession> gameSessionFunction;
    private final BiConsumer<GameSession, GamePlayerSession> onStartConsumer;

    private final BiConsumer<GameSession, GamePlayerSession> onEndConsumer;

    private final Map<UUID, GameInfo> gameInfoMap = new HashMap<>();

    public GameEngine(Function<UUID, GamePlayerSession> playerSessionFunction, Function<UUID, GameSession> gameSessionFunction, BiConsumer<GameSession, GamePlayerSession> onStartConsumer, BiConsumer<GameSession, GamePlayerSession> onEndConsumer) {
        this.playerSessionFunction = playerSessionFunction;
        this.gameSessionFunction = gameSessionFunction;
        this.onStartConsumer = onStartConsumer;
        this.onEndConsumer = onEndConsumer;
    }

    public GameSession begin(final UUID playerId) {
        ProgramCorrectnessHelper.requireState(!isPlaying(playerId), "Player %s is already in a game", playerId);

        GameSession gameSession = gameSessionFunction.apply(playerId);
        GamePlayerSession playerSession = playerSessionFunction.apply(playerId);

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

    private record GameInfo(GameSession gameSession, GamePlayerSession gamePlayerSession) {}
}
