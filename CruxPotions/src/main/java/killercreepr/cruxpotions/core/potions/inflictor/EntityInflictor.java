package killercreepr.cruxpotions.core.potions.inflictor;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.core.Crux;
import killercreepr.cruxpotions.api.potion.inflictor.LocationedInflictor;
import killercreepr.cruxpotions.api.potion.inflictor.PotionInflictor;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class EntityInflictor implements PotionInflictor, Holder<Entity>, LocationedInflictor {
    public static final String ID = "entity";
    protected final @NotNull UUID uuid;
    protected final @NotNull Holder<Entity> reference;
    protected final @Nullable Key type;

    public EntityInflictor(@NotNull Entity e){
        this.uuid = e.getUniqueId();
        this.type = e.getType().key();
        this.reference = Holder.weakReference(e);
    }

    public EntityInflictor(@NotNull UUID uuid, @Nullable Key type){
        this.uuid = uuid;
        this.type = type;
        this.reference = () -> Crux.getServer().getEntity(uuid);
    }

    public @NotNull UUID uuid() {
        return uuid;
    }

    public @Nullable Key getType() {
        return type;
    }

    @Override
    public @Nullable Entity value() {
        return reference.value();
    }

    /**
     * Used for file serialization
     */
    @Override
    public @NotNull String getTypeID() {
        return ID;
    }

    @Override
    public @Nullable Location getLocation() {
        Entity e = value();
        return e == null ? null : e.getLocation();
    }
}
