package killercreepr.cruxworlds.world.creator;

import killercreepr.crux.registry.SimpleMappedRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

public class WorldModuleCreatorRegistryImpl extends SimpleMappedRegistry<String, Collection<CruxWorldModuleCreator>> implements WorldModuleCreatorRegistry {
    public WorldModuleCreatorRegistryImpl(@NotNull Map<String, Collection<CruxWorldModuleCreator>> map) {
        super(map);
    }

    public WorldModuleCreatorRegistryImpl() {
    }

    @Override
    public <T extends CruxWorldModuleCreator> T register(@NotNull String worldName, @NotNull T creator) {
        computeIfAbsent(worldName, (name) -> new HashSet<>()).add(creator);
        return creator;
    }
}
