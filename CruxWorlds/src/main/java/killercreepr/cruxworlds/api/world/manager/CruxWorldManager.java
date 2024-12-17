package killercreepr.cruxworlds.api.world.manager;

import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.crux.api.registry.MappedRegistry;
import killercreepr.cruxworlds.api.world.CruxWorld;
import killercreepr.cruxworlds.api.world.creator.CruxWorldCreator;
import killercreepr.cruxworlds.api.world.creator.WorldModuleCreatorRegistry;
import killercreepr.cruxworlds.api.world.type.CruxWorldType;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface CruxWorldManager {
    @Nullable CruxWorld getOrCreateWorld(@NotNull Key worldType, @NotNull String name);
    @Nullable CruxWorld getOrCreateWorld(@NotNull CruxWorldType type, @NotNull String name);
    @Nullable
    CruxWorld getWorld(@NotNull String name);
    @Nullable CruxWorld getWorld(@NotNull UUID uuid);
    CompletableFuture<Boolean> deleteWorld(@NotNull CruxWorld world);
    CompletableFuture<Boolean> deleteWorld(@NotNull String world);
    CompletableFuture<Boolean> unloadWorld(@NotNull CruxWorld world, boolean save);

    default <T extends CruxWorld> @Nullable T getWorldOrNull(@NotNull String name, @NotNull Class<T> type){
        try{
            return getWorld(name, type);
        }catch (IllegalStateException ignored){
            return null;
        }
    }

    default <T extends CruxWorld> @Nullable T getWorldOrNull(@NotNull UUID uuid, @NotNull Class<T> type){
        try{
            return getWorld(uuid, type);
        }catch (IllegalStateException ignored){
            return null;
        }
    }

    default <T extends CruxWorld> @Nullable T getWorld(@NotNull String name, @NotNull Class<T> type){
        CruxWorld world = getWorld(name);
        if(world==null) return null;
        if(type.isAssignableFrom(world.getClass())) return type.cast(world);
        throw new IllegalStateException("CruxWorld cannot be cast to " + type.getSimpleName() + "! (" + world + ")");
    }

    default <T extends CruxWorld> @Nullable T getWorld(@NotNull UUID uuid, @NotNull Class<T> type){
        CruxWorld world = getWorld(uuid);
        if(world==null) return null;
        if(type.isAssignableFrom(world.getClass())) return type.cast(world);
        throw new IllegalStateException("CruxWorld cannot be cast to " + type.getSimpleName() + "! (" + world + ")");
    }

    @NotNull
    Collection<CruxWorld> getWorlds();

    @NotNull MappedRegistry<String, CruxWorldCreator> getCreatorRegistry();
    @NotNull
    KeyedRegistry<CruxWorldType> getWorldTypeRegistry();
    @NotNull
    WorldModuleCreatorRegistry getModuleCreatorRegistry();
}
