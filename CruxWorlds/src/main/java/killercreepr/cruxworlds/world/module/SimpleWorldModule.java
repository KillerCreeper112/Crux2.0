package killercreepr.cruxworlds.world.module;

import killercreepr.cruxworlds.world.CruxWorld;
import org.jetbrains.annotations.NotNull;

public class SimpleWorldModule implements WorldModule{
    protected final @NotNull CruxWorld parent;
    public SimpleWorldModule(@NotNull CruxWorld parent) {
        this.parent = parent;
    }

    public @NotNull CruxWorld getParent() {
        return parent;
    }
}
