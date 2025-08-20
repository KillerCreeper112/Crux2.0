package killercreepr.cruxworlds.api.world.manager;

import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.crux.api.registry.MappedRegistry;
import killercreepr.cruxworlds.api.world.CruxWorld;
import killercreepr.cruxworlds.api.world.creator.CruxWorldCreator;
import killercreepr.cruxworlds.api.world.creator.WorldModuleCreatorRegistry;
import killercreepr.cruxworlds.api.world.type.CruxWorldType;
import killercreepr.cruxworlds.core.component.CruxWorldsComponents;
import net.kyori.adventure.key.Key;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface CruxWorldManager {
    default @Nullable CruxWorld getOrCreateWorld(@NotNull CruxWorldType type){
        return getOrCreateWorld(type, type.defaultWorldKey());
    }

    CruxWorldType getWorldType(@NotNull World world);
    CruxWorldType getWorldType(@NotNull CruxWorld world);

    @Nullable CruxWorld getOrCreateWorld(@NotNull Key worldType, @NotNull Key name);
    @Nullable CruxWorld getOrCreateWorld(@NotNull CruxWorldType type, @NotNull Key name);
    @Nullable
    CruxWorld getWorld(@NotNull Key name);
    //@Nullable CruxWorld getWorld(@NotNull UUID uuid);
    CompletableFuture<Boolean> deleteWorld(@NotNull CruxWorld world);
    CompletableFuture<Boolean> deleteWorld(@NotNull Key world);
    CompletableFuture<Boolean> unloadWorld(@NotNull CruxWorld world, boolean save);
    CompletableFuture<CruxWorld> loadWorld(@NotNull Key worldName);

    default <T extends CruxWorld> @Nullable T getWorldOrNull(@NotNull Key name, @NotNull Class<T> type){
        try{
            return getWorld(name, type);
        }catch (IllegalStateException ignored){
            return null;
        }
    }

    /*default <T extends CruxWorld> @Nullable T getWorldOrNull(@NotNull Key uuid, @NotNull Class<T> type){
        try{
            return getWorld(uuid, type);
        }catch (IllegalStateException ignored){
            return null;
        }
    }*/

    default <T extends CruxWorld> @Nullable T getWorld(@NotNull Key name, @NotNull Class<T> type){
        CruxWorld world = getWorld(name);
        if(world==null) return null;
        if(type.isAssignableFrom(world.getClass())) return type.cast(world);
        throw new IllegalStateException("CruxWorld cannot be cast to " + type.getSimpleName() + "! (" + world + ")");
    }

    /*default <T extends CruxWorld> @Nullable T getWorld(@NotNull Key uuid, @NotNull Class<T> type){
        CruxWorld world = getWorld(uuid);
        if(world==null) return null;
        if(type.isAssignableFrom(world.getClass())) return type.cast(world);
        throw new IllegalStateException("CruxWorld cannot be cast to " + type.getSimpleName() + "! (" + world + ")");
    }*/

    @NotNull
    Collection<CruxWorld> getWorlds();

    @NotNull MappedRegistry<Key, CruxWorldCreator> getCreatorRegistry();
    @NotNull
    KeyedRegistry<CruxWorldType> getWorldTypeRegistry();
    @NotNull
    WorldModuleCreatorRegistry getModuleCreatorRegistry();

    default boolean isWorldType(World world, CruxWorldType type){
        CruxWorld crux = getWorld(world.key());
        if(crux == null) return false;
        return type.compare(crux.get(CruxWorldsComponents.WORLD_TYPE));
    }
    default boolean isAnyWorldType(World world, @Nullable CruxWorldType... types){
        CruxWorld crux = getWorld(world.key());
        if(crux == null) return false;
        var type = crux.get(CruxWorldsComponents.WORLD_TYPE);
        for(CruxWorldType compare : types){
            if(compare == null){
                if(type == null) return true;
                continue;
            }
            if(type == null) continue;
            if(type.compare(compare)) return true;
        }
        return false;
    }
}
