package killercreepr.cruxentities.entity;

import killercreepr.crux.util.CruxString;
import killercreepr.cruxentities.persistence.CruxEntitiesPersistTags;
import killercreepr.cruxentities.registries.CruxEntityRegistries;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CruxMob extends Keyed {
    static boolean is(@NotNull Entity e, @NotNull CruxMob@NotNull... grim){
        for(CruxMob x : grim){
            if(is(e, x)) return true;
        }
        return false;
    }

    static boolean is(@NotNull Entity e){
        return CruxEntitiesPersistTags.ENTITY.has(e);
    }

    static boolean is(@NotNull Entity e, @NotNull CruxMob grim){
        return grim.key().equals(CruxEntitiesPersistTags.ENTITY.get(e, null));
    }

    static <T extends PersistentDataHolder> @Nullable Key getKey(@NotNull T e){
        return CruxEntitiesPersistTags.ENTITY.get(e);
    }

    static <T extends PersistentDataHolder> @Nullable CruxMob get(@NotNull T e){
        Key key = getKey(e);
        return key==null?null: CruxEntityRegistries.ENTITIES.get(key);
    }

    default void load(@NotNull Entity e){};
    @NotNull Entity spawn(@NotNull Location at);
    default @NotNull String getName(){
        return CruxString.toTitleCase(key().value());
    }
}
