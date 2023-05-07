package com.github.tofpu.gameengine;

import com.github.tofpu.gameengine.island.DemoIsland;
import com.github.tofpu.gameengine.island.DemoIslandService;
import com.github.tofpu.gameengine.island.Island;
import com.github.tofpu.gameengine.island.IslandService;
import com.github.tofpu.gameengine.island.arena.ArenaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

import static com.github.tofpu.gameengine.util.ProgramCorrectnessHelper.requireArgument;

public class GameEngineTest {
    @Test
    public void simple_test() {
        IslandService islandService = new DemoIslandService();
        islandService.register(DemoIsland.create(1, 50));

        ArenaService arenaService = new ArenaService(() -> 10);

        GameEngine gameEngine = GameEngine.create(
                (uuid, objects) -> new IslandPlayerSession(uuid),
                (uuid, objects) -> {
                    requireArgument(objects.length == 1, "Unknown arguments %s", Arrays.toString(objects));
                    requireArgument(objects[0] instanceof Integer, "Must be an integer: %s", objects[0]);

                    int islandSlot = (int) objects[0];
                    return new IslandGameSession(uuid, islandSlot);
                },
                (game, player) -> {
                    // teleport player to world, clear their inventory, etc.
                    IslandGameSession gameSession = (IslandGameSession) game;
                    int islandSlot = gameSession.getIslandSlot();
                    Island island = islandService.get(islandSlot);

                    arenaService.reserveLand(island);
                },
                (game, player) -> {
                    // clear their inventory, teleport the player to lobby, etc etc
                    IslandGameSession gameSession = (IslandGameSession) game;
                    int islandSlot = gameSession.getIslandSlot();
                    Island island = islandService.get(islandSlot);

                    arenaService.unreserveLand(island);
                }
        );

        UUID playerId = UUID.randomUUID();
        Island island = islandService.get(1);

        GameSession gameSession = gameEngine.begin(playerId, 1);
        Assertions.assertNotNull(gameSession);
        Assertions.assertTrue(gameEngine.isPlaying(playerId));

        Assertions.assertTrue(arenaService.isReserved(island));

        GameSession endSession = gameEngine.end(playerId);
        Assertions.assertNotNull(endSession);
        Assertions.assertFalse(gameEngine.isPlaying(playerId));
        Assertions.assertFalse(arenaService.isReserved(island));
    }
}
