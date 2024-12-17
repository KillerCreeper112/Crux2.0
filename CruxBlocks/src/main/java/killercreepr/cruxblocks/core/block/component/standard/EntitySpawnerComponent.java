package killercreepr.cruxblocks.core.block.component.standard;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.api.block.component.CruxBlockComponent;
import killercreepr.cruxblocks.core.block.active.standard.ActiveEntitySpawner;
import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawnGroup;
import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawner;
import killercreepr.cruxworlds.core.world.entity.SimpleNaturalEntitySpawner;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class EntitySpawnerComponent implements CruxBlockComponent {
    public final @NotNull NumberProvider spawnDelay;
    public final @NotNull NumberProvider spawnRange;
    public final @NotNull NumberProvider innerSpawnDistance;
    public final @NotNull NumberProvider spawnCount;
    public final @NotNull NumberProvider requiredPlayerRange;
    public final @NotNull NumberProvider maxSpawnAttempts;
    public final @NotNull NumberProvider groupSpawnAmount;
    public final @NotNull NumberProvider yCheck;
    public final @NotNull NumberProvider failedDelay;
    public final @NotNull Collection<NaturalEntitySpawnGroup> spawns;
    public final @NotNull NaturalEntitySpawner spawner;
    public final boolean ignoreCreativePlayers;

    public EntitySpawnerComponent(@NotNull NumberProvider spawnDelay,
                                  @NotNull NumberProvider spawnRange, @NotNull NumberProvider innerSpawnDistance,
                                  @NotNull NumberProvider spawnCount,
                                  @NotNull NumberProvider requiredPlayerRange, @NotNull NumberProvider maxSpawnAttempts,
                                  @NotNull NumberProvider groupSpawnAmount, @NotNull NumberProvider yCheck, @NotNull NumberProvider failedDelay,
                                  @NotNull Collection<NaturalEntitySpawnGroup> spawns, boolean ignoreCreativePlayers) {
        this.spawnDelay = spawnDelay;
        this.spawnRange = spawnRange;
        this.innerSpawnDistance = innerSpawnDistance;
        this.spawnCount = spawnCount;
        this.requiredPlayerRange = requiredPlayerRange;
        this.maxSpawnAttempts = maxSpawnAttempts;
        this.groupSpawnAmount = groupSpawnAmount;
        this.yCheck = yCheck;
        this.failedDelay = failedDelay;
        this.spawns = spawns;
        this.ignoreCreativePlayers = ignoreCreativePlayers;
        spawner = new SimpleNaturalEntitySpawner(
            Crux.getMainPlugin(), CruxMath.random(), spawns,
            spawnRange,
            innerSpawnDistance,
            spawnCount, maxSpawnAttempts,
            groupSpawnAmount, yCheck
        );
    }

    @Override
    public @Nullable ActiveCruxBlock createActive(@NotNull Block block, @NotNull CruxBlock crux) {
        return new ActiveEntitySpawner(block, crux, this, spawner);
    }
}
