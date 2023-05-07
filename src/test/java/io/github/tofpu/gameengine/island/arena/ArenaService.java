package io.github.tofpu.gameengine.island.arena;

import io.github.tofpu.gameengine.island.arena.land.Position;
import io.github.tofpu.gameengine.island.arena.land.ArenaLand;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.function.Supplier;

import static io.tofpu.gameengine.util.ProgramCorrectnessHelper.requireState;

public class ArenaService {
    private final Map<Object, ArenaLand> arenaMap = new HashMap<>();
    private final Queue<Position> unreservedQueue = new LinkedList<>();

    private final Counter xCounter = new Counter();

    private final Supplier<Integer> landGapSupplier;

    public ArenaService(Supplier<Integer> landGapSupplier) {
        this.landGapSupplier = landGapSupplier;
    }

    public ArenaLand reserveLand(final SizeableObject obj) {
        requireState(!isReserved(obj), "%s has already reserved a land.", obj);
        return reserve(obj);
    }

    public ArenaLand unreserveLand(final SizeableObject obj) {
        requireState(isReserved(obj), "%s does not have a reserved land.", obj);
        return unreserve(obj);
    }

    @NotNull
    private ArenaLand unreserve(SizeableObject obj) {
        ArenaLand arenaLand = this.arenaMap.remove(obj);

        this.unreservedQueue.add(arenaLand.getPosition());

        return arenaLand;
    }

    @NotNull
    private ArenaLand reserve(SizeableObject obj) {
        Position position;
        if (!this.unreservedQueue.isEmpty()) {
            position = this.unreservedQueue.poll();
        } else {
            int x = xCounter.incrementBy(obj.width(), landGapSupplier.get());
            position = new Position(x, 0, 0);
        }

        ArenaLand arenaLand = new ArenaLand(position);
        this.arenaMap.put(obj, arenaLand);

        return arenaLand;
    }

    public boolean isReserved(final Object obj) {
        return this.arenaMap.containsKey(obj);
    }
}
