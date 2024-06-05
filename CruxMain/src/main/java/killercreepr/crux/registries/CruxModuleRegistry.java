package killercreepr.crux.registries;

import killercreepr.crux.module.CruxModule;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.registry.SimpleMappedRegistry;
import org.jetbrains.annotations.NotNull;

public class CruxModuleRegistry extends SimpleMappedRegistry<String, CruxModule> {
    @Override
    public @NotNull CruxModule register(@NotNull CruxModule object) {
        return register(object.name(), object);
    }

    @Override
    public boolean unregister(@NotNull CruxModule object) {
        return remove(object.name()) != null;
    }

    /**
     * Registers and enabled only the provided modules.
     */
    public CruxModuleRegistry registerAndEnable(@NotNull CruxPlugin plugin, @NotNull CruxModule... modules){
        for(CruxModule m : modules){
            register(m);
        }
        for(CruxModule m : modules){
            m.onEnable(plugin);
        }
        return this;
    }

    public CruxModuleRegistry register(@NotNull CruxModule... modules){
        for(CruxModule m : modules){
            register(m);
        }
        return this;
    }

    public CruxModuleRegistry enable(@NotNull CruxPlugin plugin){
        for(CruxModule m : this){
            m.onEnable(plugin);
        }
        return this;
    }

    public CruxModuleRegistry unregisterAll(@NotNull CruxPlugin plugin){
        for(CruxModule m : this){
            m.onDisable(plugin);
        }
        clear();
        return this;
    }

    public CruxModuleRegistry reload(@NotNull CruxPlugin plugin){
        for(CruxModule m : this){
            m.reload(plugin);
        }
        return this;
    }
}
