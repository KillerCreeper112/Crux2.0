package killercreepr.crux.registries;

import killercreepr.crux.module.CruxModule;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import org.jetbrains.annotations.NotNull;

public class Registries {
    public static final MappedRegistry<String, CruxPlugin> PLUGINS = new SimpleMappedRegistry<>(){
        @Override
        public @NotNull CruxPlugin register(@NotNull CruxPlugin object) {
            return register(object.getName(), object);
        }

        @Override
        public boolean unregister(@NotNull CruxPlugin object) {
            return remove(object.getName()) != null;
        }
    };

    public static final MappedRegistry<String, Boolean> BOOLEAN_MAPPED = SimpleMappedRegistry.fromHashMap();
    public static final MappedRegistry<String, CruxModule> MODULES = SimpleMappedRegistry.fromHashMap();
    //public static final MappedRegistry<String, PlayerPlaceholder> PLAYER_PLACEHOLDERS = SimpleMappedRegistry.fromHashMap();
}
