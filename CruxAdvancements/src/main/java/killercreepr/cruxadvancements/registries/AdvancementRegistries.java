package killercreepr.cruxadvancements.registries;

import killercreepr.crux.registry.Registry;
import killercreepr.crux.registry.SimpleRegistry;
import killercreepr.cruxadvancements.data.TrackedAdvancement;

public class AdvancementRegistries {
    public static final CruxAdvancementManagerRegistry ADVANCEMENT_MANAGERS = new CruxAdvancementManagerRegistry();
    public static final Registry<TrackedAdvancement> GLOBAL_ADVANCEMENTS = SimpleRegistry.fromSet();
}
