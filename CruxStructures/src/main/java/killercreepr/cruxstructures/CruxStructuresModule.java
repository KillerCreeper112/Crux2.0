package killercreepr.cruxstructures;

import killercreepr.crux.module.CruxModule;
import org.jetbrains.annotations.NotNull;

public class CruxStructuresModule implements CruxModule {
    public static final String NAMESPACE = "CruxStructures";
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }
}
