package killercreepr.crux.registries;

import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.SimpleMappedRegistry;

public class Registries {
    public static final MappedRegistry<String, Boolean> BOOLEAN_MAPPED = SimpleMappedRegistry.fromHashMap();
    //public static final MappedRegistry<String, PlayerPlaceholder> PLAYER_PLACEHOLDERS = SimpleMappedRegistry.fromHashMap();
}
