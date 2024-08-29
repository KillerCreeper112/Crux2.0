package killercreepr.cruxworlds.world.manager;

import killercreepr.crux.registry.MappedRegistry;
import killercreepr.cruxworlds.world.CruxWorld;
import killercreepr.cruxworlds.world.creator.CruxWorldCreator;
import killercreepr.cruxworlds.world.creator.WorldModuleCreatorRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

public interface CruxWorldManager {
    @Nullable
    CruxWorld getWorld(@NotNull String name);
    @Nullable CruxWorld getWorld(@NotNull UUID uuid);

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
    WorldModuleCreatorRegistry getModuleCreatorRegistry();
}
