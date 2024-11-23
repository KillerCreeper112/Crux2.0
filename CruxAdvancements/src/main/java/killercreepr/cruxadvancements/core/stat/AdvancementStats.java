package killercreepr.cruxadvancements.core.stat;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxadvancements.api.values.ValuesProvider;
import killercreepr.cruxadvancements.core.CruxAdvancementsModule;
import killercreepr.cruxstats.api.stat.CruxStat;
import killercreepr.cruxstats.core.registries.CruxStatRegistries;

public class AdvancementStats {
    public static void register(){}
    public static final CruxStat MAX_TRACKABLE_ADVANCEMENTS = CruxStatRegistries.STAT.register(
        CruxStat.stat(Crux.key("max_trackable_advancements"), NumberProvider.holder(() -> cfg().DEFAULT_MAX_TRACKED_ADVANCEMENTS().value()))
    );

    public static ValuesProvider cfg(){
        return CruxRegistries.MODULES.getModuleOrThrow(CruxAdvancementsModule.class).values();
    }
}
