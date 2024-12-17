package killercreepr.cruxworlds.core.world.entity;

import killercreepr.crux.core.loot.SimpleWeighted;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawn;
import killercreepr.cruxworlds.api.world.entity.SpawnContext;
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
