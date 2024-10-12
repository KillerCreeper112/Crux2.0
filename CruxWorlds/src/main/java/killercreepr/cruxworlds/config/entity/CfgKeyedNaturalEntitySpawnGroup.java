package killercreepr.cruxworlds.config.entity;

import killercreepr.cruxworlds.world.entity.KeyedNaturalEntitySpawnGroup;
import killercreepr.cruxworlds.world.entity.NaturalEntitySpawn;
import killercreepr.cruxworlds.world.spawning.SpawnValidator;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class CfgKeyedNaturalEntitySpawnGroup extends CfgNaturalEntitySpawnGroup implements KeyedNaturalEntitySpawnGroup {
    protected final @NotNull Key key;

    public CfgKeyedNaturalEntitySpawnGroup(int weight, float quality, @NotNull Collection<NaturalEntitySpawn> spawns, @Nullable SpawnValidator spawnValidator, @NotNull Key key) {
        super(weight, quality, spawns, spawnValidator);
        this.key = key;
    }

    public CfgKeyedNaturalEntitySpawnGroup(int weight, float quality, @NotNull Key key, @Nullable SpawnValidator spawnValidator, @NotNull NaturalEntitySpawn... spawns) {
        super(weight, quality, spawnValidator, spawns);
        this.key = key;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
