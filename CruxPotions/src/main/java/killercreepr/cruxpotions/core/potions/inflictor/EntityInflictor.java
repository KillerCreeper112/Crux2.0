package killercreepr.cruxpotions.core.potions.inflictor;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.core.Crux;
import killercreepr.cruxpotions.api.potion.inflictor.PotionInflictor;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public class EntityInflictor implements PotionInflictor, Holder<Entity> {
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
}
