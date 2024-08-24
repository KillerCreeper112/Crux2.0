package killercreepr.cruxentities.world.entity.entity.impl;

import killercreepr.crux.loot.impl.SimpleWeighted;
import killercreepr.crux.util.CruxMath;
import killercreepr.cruxentities.world.entity.entity.NaturalEntitySpawn;
import killercreepr.cruxentities.world.entity.entity.SpawnContext;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleNaturalEntitySpawn extends SimpleWeighted implements NaturalEntitySpawn {
    public SimpleNaturalEntitySpawn(int weight, float quality) {
        super(weight, quality);
    }

    public int getGroupSize(@NotNull SpawnContext ctx){ return 1; }
    public int getGroupRadius(@NotNull SpawnContext ctx){ return CruxMath.random(5, 10); }

    protected boolean isPassableAndNotLiquid(@NotNull Block b){
        return b.isPassable() && !b.isLiquid();
    }
}
