package com.github.tofpu.gameengine.island;

import java.util.HashMap;
import java.util.Map;

import static com.github.tofpu.gameengine.util.ProgramCorrectnessHelper.requireState;

public class DemoIslandService implements IslandService {
    private final Map<Integer, Island> islandMap = new HashMap<>();

    @Override
    public Island register(Island island) {
        requireState(get(island.getSlot()) == null, "Island %s already exists", island.getSlot());

        islandMap.put(island.getSlot(), island);

        return island;
    }

    @Override
    public Island get(int slot) {
        return islandMap.get(slot);
    }
}
