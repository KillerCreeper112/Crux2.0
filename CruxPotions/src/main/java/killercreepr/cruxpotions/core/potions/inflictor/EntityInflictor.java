package killercreepr.cruxpotions.core.potions.inflictor;

import killercreepr.crux.api.data.Holder;
import killercreepr.cruxpotions.api.potion.inflictor.PotionInflictor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public class EntityInflictor implements PotionInflictor, Holder<Entity> {
    protected final @NotNull UUID uuid;
    protected final @Nullable EntityType type;
    public EntityInflictor(@NotNull UUID uuid) {
        this.uuid = uuid;
        Entity e = value();
        type = e == null ? null : e.getType();
    }

    public EntityInflictor(@NotNull UUID uuid, @Nullable EntityType type) {
        this.uuid = uuid;
        this.type = type;
    }

    public EntityInflictor(@NotNull Entity e){
        this.uuid = e.getUniqueId();
        this.type = e.getType();
    }

    public @NotNull UUID uuid() {
        return uuid;
    }

    public @Nullable EntityType type() {
        return type;
    }

    @Override
    public @Nullable Entity value() {
        return Bukkit.getEntity(uuid);
    }
}
