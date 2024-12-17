package killercreepr.cruxworlds.core.world.module;

import killercreepr.cruxworlds.api.world.CruxWorld;
import killercreepr.cruxworlds.api.world.module.WorldModule;
import org.jetbrains.annotations.NotNull;

public class SimpleWorldModule implements WorldModule {
    protected final @NotNull CruxWorld parent;
    public SimpleWorldModule(@NotNull CruxWorld parent) {
        this.parent = parent;
    }

    public @NotNull CruxWorld getParent() {
        return parent;
    }
}
