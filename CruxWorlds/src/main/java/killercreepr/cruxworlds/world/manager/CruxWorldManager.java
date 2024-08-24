package killercreepr.cruxworlds.world.manager;

import killercreepr.crux.registry.MappedRegistry;
import killercreepr.cruxworlds.world.CruxWorld;
import killercreepr.cruxworlds.world.creator.CruxWorldCreator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

public interface CruxWorldManager {
    @Nullable
    CruxWorld getWorld(@NotNull String name);
    @Nullable CruxWorld getWorld(@NotNull UUID uuid);
    @NotNull
    Collection<CruxWorld> getWorlds();

    @NotNull MappedRegistry<String, CruxWorldCreator> getCreatorRegistry();
}
