package killercreepr.crux.core.loot.conditions.world;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.core.loot.conditions.BaseCondition;
import killercreepr.crux.paper.nms.biome.BiomeUtils;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LocationCondition extends BaseCondition {
    protected final @Nullable String worldName;
    protected final @Nullable String worldDimension;
    protected final @Nullable Key biome;

    public LocationCondition(@NotNull String target, @Nullable String worldName, @Nullable String worldDimension, @Nullable Key biome) {
        super(target);
        this.worldName = worldName;
        this.worldDimension = worldDimension;
        this.biome = biome;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Location b = ctx.info().get(target, Location.class);
        if(b==null) return false;
        if(worldName != null && !b.getWorld().getName().equals(worldName)) return false;
        if(worldDimension != null && !b.getWorld().getEnvironment().toString().equalsIgnoreCase(worldDimension)) return false;
        if(biome != null && !BiomeUtils.getBiome(b).equals(biome)) return false;
        return true;
    }
}
