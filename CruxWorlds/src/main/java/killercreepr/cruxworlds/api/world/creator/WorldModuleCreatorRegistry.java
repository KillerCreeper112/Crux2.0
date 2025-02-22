package killercreepr.cruxworlds.api.world.creator;

import killercreepr.crux.api.registry.MappedRegistry;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface WorldModuleCreatorRegistry extends MappedRegistry<Key, Collection<CruxWorldModuleCreator>> {
    <T extends CruxWorldModuleCreator> T register(@NotNull Key worldName, @NotNull T creator);
}
