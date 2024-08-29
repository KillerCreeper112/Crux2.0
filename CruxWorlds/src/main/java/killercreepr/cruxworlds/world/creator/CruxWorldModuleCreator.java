package killercreepr.cruxworlds.world.creator;

import killercreepr.cruxworlds.world.CruxWorld;
import killercreepr.cruxworlds.world.module.WorldModule;
import org.jetbrains.annotations.NotNull;

public interface CruxWorldModuleCreator {
    @NotNull
    WorldModule create(@NotNull CruxWorld world);
}
