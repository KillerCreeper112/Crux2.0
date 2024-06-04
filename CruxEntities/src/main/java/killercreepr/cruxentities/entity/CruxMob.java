package killercreepr.cruxentities.entity;

import killercreepr.crux.util.CruxString;
import killercreepr.cruxentities.persistence.EntityPersistTags;
import killercreepr.cruxentities.registries.CruxEntityRegistries;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.*;
import org.bukkit.entity.*;
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
        return EntityPersistTags.ENTITY.has(e);
    }

    static boolean is(@NotNull Entity e, @NotNull CruxMob grim){
        return grim.key().equals(EntityPersistTags.ENTITY.get(e, null));
    }

    static <T extends PersistentDataHolder> @Nullable Key getKey(@NotNull T e){
        return EntityPersistTags.ENTITY.get(e);
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
