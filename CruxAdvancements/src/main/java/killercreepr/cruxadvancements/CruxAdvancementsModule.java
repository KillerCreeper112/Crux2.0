package killercreepr.cruxadvancements;

import killercreepr.crux.module.CruxModule;
import org.jetbrains.annotations.NotNull;

//todo
public class CruxAdvancementsModule implements CruxModule {
    public static final String NAMESPACE = "CruxAdvancements";
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }
}
