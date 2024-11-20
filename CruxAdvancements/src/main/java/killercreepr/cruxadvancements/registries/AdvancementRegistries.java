package killercreepr.cruxadvancements.registries;

import killercreepr.crux.api.registry.Registry;
import killercreepr.crux.core.registry.SimpleRegistry;
import killercreepr.cruxadvancements.data.TrackedAdvancement;

public class AdvancementRegistries {
    public static final CruxAdvancementManagerRegistry ADVANCEMENT_MANAGERS = new CruxAdvancementManagerRegistry();
    public static final Registry<TrackedAdvancement> GLOBAL_ADVANCEMENTS = SimpleRegistry.fromSet();
}
