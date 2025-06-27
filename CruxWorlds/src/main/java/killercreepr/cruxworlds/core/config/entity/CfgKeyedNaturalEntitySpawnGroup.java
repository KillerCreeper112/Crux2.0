package killercreepr.cruxworlds.core.config.entity;

import killercreepr.cruxworlds.api.world.entity.KeyedNaturalEntitySpawnGroup;
import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawn;
import killercreepr.cruxworlds.api.world.spawning.SpawnValidator;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class CfgKeyedNaturalEntitySpawnGroup extends CfgNaturalEntitySpawnGroup implements KeyedNaturalEntitySpawnGroup {
    protected final @NotNull Key key;

    public CfgKeyedNaturalEntitySpawnGroup(int weight, float quality, @NotNull Collection<NaturalEntitySpawn> spawns, @Nullable SpawnValidator spawnValidator, int rolls, @NotNull Key key) {
        super(weight, quality, spawns, spawnValidator, rolls);
        this.key = key;
    }

    public CfgKeyedNaturalEntitySpawnGroup(int weight, float quality, @NotNull Key key, @Nullable SpawnValidator spawnValidator, int rolls, @NotNull NaturalEntitySpawn... spawns) {
        super(weight, quality, spawnValidator, rolls, spawns);
        this.key = key;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
