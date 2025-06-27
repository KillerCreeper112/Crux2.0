package killercreepr.cruxentities.component;

import killercreepr.crux.api.loot.LootTable;
import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawnGroup;

public class CreatureSpawnerCfg {
    protected final LootTable<NaturalEntitySpawnGroup> spawns;

    public CreatureSpawnerCfg(LootTable<NaturalEntitySpawnGroup> spawns) {
        this.spawns = spawns;
    }

    public LootTable<NaturalEntitySpawnGroup> getSpawns() {
        return spawns;
    }
}
