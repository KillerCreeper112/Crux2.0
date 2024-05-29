package killercreepr.cruxpotion.potions.inflictor;

import killercreepr.crux.data.Holder;
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
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "uuid", uuid.toString(),
                "type", type + ""
        );
    }

    public static @NotNull EntityInflictor deserialize(@NotNull Map< String, Object> map){
        UUID uuid;
        try{
            uuid = UUID.fromString((String) map.getOrDefault("uuid", ""));
        }catch (IllegalArgumentException e){
            throw new RuntimeException("UUID is not parsable!");
        }
        EntityType type = null;
        if(map.containsKey("type")){
            try {
                type = EntityType.valueOf(map.getOrDefault("type", "").toString().toUpperCase());
            } catch (Exception ignored){}
        }
        return new EntityInflictor(uuid, type);
    }

    @Override
    public @Nullable Entity value() {
        return Bukkit.getEntity(uuid);
    }
}
