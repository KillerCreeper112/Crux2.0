package killercreepr.cruxconfig;

import killercreepr.crux.module.CruxModule;
import org.jetbrains.annotations.NotNull;

public class CruxConfigsModule implements CruxModule {
    public static final String NAMESPACE = "CruxConfigs";
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }
}
