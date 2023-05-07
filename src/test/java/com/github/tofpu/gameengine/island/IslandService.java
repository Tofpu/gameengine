package com.github.tofpu.gameengine.island;

public interface IslandService {
    Island register(Island island);
    Island get(int slot);
}
