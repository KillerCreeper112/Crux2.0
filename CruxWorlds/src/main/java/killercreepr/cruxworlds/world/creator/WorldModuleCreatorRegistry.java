package killercreepr.cruxworlds.world.creator;

import killercreepr.crux.registry.MappedRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface WorldModuleCreatorRegistry extends MappedRegistry<String, Collection<CruxWorldModuleCreator>> {
    <T extends CruxWorldModuleCreator> T register(@NotNull String worldName, @NotNull T creator);
}
