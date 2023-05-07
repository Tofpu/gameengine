package io.github.tofpu.gameengine;

import io.github.tofpu.gameengine.island.arena.ArenaService;
import io.tofpu.gameengine.GameEngine;
import io.tofpu.gameengine.GameSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class GameEngineTest {
    @Test
    public void simple_test() {
        ArenaService arenaService = new ArenaService(() -> 10);

        GameEngine gameEngine = GameEngine.create(
                IslandPlayerSession::new,
                IslandGameSession::new,
                (game, player) -> {
                    // teleport player to world, clear their inventory, etc etc
//                    arenaService.reserveLand(game);
                },
                (game, player) -> {
                    // clear their inventory, teleport the player to lobby, etc etc
//                    arenaService.unreserveLand(game);
                }
        );

        UUID playerId = UUID.randomUUID();

        GameSession gameSession = gameEngine.begin(playerId);
        Assertions.assertNotNull(gameSession);
        Assertions.assertTrue(gameEngine.isPlaying(playerId));

        GameSession endSession = gameEngine.end(playerId);
        Assertions.assertNotNull(endSession);
        Assertions.assertFalse(gameEngine.isPlaying(playerId));
    }
}
