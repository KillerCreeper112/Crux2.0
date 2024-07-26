package killercreepr.cruxadvancements;

import killercreepr.crux.module.CruxModule;
import killercreepr.crux.module.StandardModules;
import org.jetbrains.annotations.NotNull;

//todo
public class CruxAdvancementsModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_ADVANCEMENTS;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }
}
