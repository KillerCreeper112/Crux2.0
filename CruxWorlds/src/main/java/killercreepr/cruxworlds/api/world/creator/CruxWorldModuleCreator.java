package killercreepr.cruxworlds.api.world.creator;

import killercreepr.cruxworlds.api.world.CruxWorld;
import killercreepr.cruxworlds.api.world.module.WorldModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CruxWorldModuleCreator {
    @Nullable
    WorldModule create(@NotNull CruxWorld world);
}
