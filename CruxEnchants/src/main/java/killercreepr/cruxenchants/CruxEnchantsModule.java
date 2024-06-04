package killercreepr.cruxenchants;

import killercreepr.crux.module.CruxModule;
import org.jetbrains.annotations.NotNull;

public class CruxEnchantsModule implements CruxModule {
    public static final String NAMESPACE = "CruxEnchants";
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }
}
