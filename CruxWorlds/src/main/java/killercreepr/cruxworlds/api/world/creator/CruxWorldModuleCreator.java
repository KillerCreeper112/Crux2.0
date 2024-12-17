package killercreepr.cruxworlds.api.world.creator;

import killercreepr.cruxworlds.api.world.CruxWorld;
import killercreepr.cruxworlds.api.world.module.WorldModule;
import org.jetbrains.annotations.NotNull;

public interface CruxWorldModuleCreator {
    @NotNull
    WorldModule create(@NotNull CruxWorld world);
}
