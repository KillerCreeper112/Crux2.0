package killercreepr.cruxworlds.core.world.creator;

import killercreepr.crux.core.registry.SimpleMappedRegistry;
import killercreepr.cruxworlds.api.world.creator.CruxWorldModuleCreator;
import killercreepr.cruxworlds.api.world.creator.WorldModuleCreatorRegistry;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

public class WorldModuleCreatorRegistryImpl extends SimpleMappedRegistry<Key, Collection<CruxWorldModuleCreator>> implements WorldModuleCreatorRegistry {
    public WorldModuleCreatorRegistryImpl(@NotNull Map<Key, Collection<CruxWorldModuleCreator>> map) {
        super(map);
    }

    public WorldModuleCreatorRegistryImpl() {
    }

    @Override
    public <T extends CruxWorldModuleCreator> T register(@NotNull Key worldName, @NotNull T creator) {
        computeIfAbsent(worldName, (name) -> new HashSet<>()).add(creator);
        return creator;
    }
}
