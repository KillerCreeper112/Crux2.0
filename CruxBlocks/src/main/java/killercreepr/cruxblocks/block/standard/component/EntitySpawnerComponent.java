package killercreepr.cruxblocks.block.standard.component;

import killercreepr.crux.Crux;
import killercreepr.crux.util.CruxMath;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.component.CruxBlockComponent;
import killercreepr.cruxblocks.block.standard.active.ActiveEntitySpawner;
import killercreepr.cruxworlds.world.entity.NaturalEntitySpawnGroup;
import killercreepr.cruxworlds.world.entity.NaturalEntitySpawner;
import killercreepr.cruxworlds.world.entity.impl.SimpleNaturalEntitySpawner;
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
    public final @NotNull Collection<NaturalEntitySpawnGroup> spawns;
    public final @NotNull NaturalEntitySpawner spawner;
    public final boolean ignoreCreativePlayers;

    public EntitySpawnerComponent(@NotNull NumberProvider spawnDelay,
                                  @NotNull NumberProvider spawnRange, @NotNull NumberProvider innerSpawnDistance,
                                  @NotNull NumberProvider spawnCount,
                                  @NotNull NumberProvider requiredPlayerRange, @NotNull NumberProvider maxSpawnAttempts,
                                  @NotNull NumberProvider groupSpawnAmount, @NotNull NumberProvider yCheck,
                                  @NotNull Collection<NaturalEntitySpawnGroup> spawns, boolean ignoreCreativePlayers) {
        this.spawnDelay = spawnDelay;
        this.spawnRange = spawnRange;
        this.innerSpawnDistance = innerSpawnDistance;
        this.spawnCount = spawnCount;
        this.requiredPlayerRange = requiredPlayerRange;
        this.maxSpawnAttempts = maxSpawnAttempts;
        this.groupSpawnAmount = groupSpawnAmount;
        this.yCheck = yCheck;
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
