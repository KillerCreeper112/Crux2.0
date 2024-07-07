package killercreepr.cruxadvancements;

import eu.endercentral.crazy_advancements.advancement.Advancement;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.plugin.CruxPlugin;
import org.jetbrains.annotations.NotNull;

//todo
public class CruxAdvancementsModule implements CruxModule {
    public static final String NAMESPACE = "CruxAdvancements";
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }
}
