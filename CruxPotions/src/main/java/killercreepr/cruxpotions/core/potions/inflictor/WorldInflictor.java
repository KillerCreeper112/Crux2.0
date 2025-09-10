package killercreepr.cruxpotions.core.potions.inflictor;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.core.Crux;
import killercreepr.cruxpotions.api.potion.inflictor.PotionInflictor;
import net.kyori.adventure.key.Key;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class WorldInflictor implements PotionInflictor, Holder<World> {
    public static final String ID = "world";
    protected final @NotNull Key world;

    public WorldInflictor(@NotNull Key world) {
        this.world = world;
    }

    public WorldInflictor(World b) {
        this(b.key());
    }

    public @NotNull Key getWorld() {
        return world;
    }

    @Override
    public World value() {
        return Crux.getServer().getWorld(this.world);
    }

    /**
     * Used for file serialization
     */
    @Override
    public @NotNull String getTypeID() {
        return ID;
    }
}
