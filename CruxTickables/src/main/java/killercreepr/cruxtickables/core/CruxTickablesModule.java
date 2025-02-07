package killercreepr.cruxtickables.core;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.entity.memory.PlayerMemory;
import killercreepr.crux.api.plugin.module.CruxModule;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.plugin.module.StandardModules;
import killercreepr.cruxtickables.core.component.CruxTickableComponents;
import killercreepr.cruxtickables.core.entity.memory.EntityTickablesHolder;
import killercreepr.cruxtickables.core.listener.TickableListener;
import org.jetbrains.annotations.NotNull;

public class CruxTickablesModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_TICKABLES;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    @Override
    public void onLoad(@NotNull CruxPlugin plugin) {
        CruxTickableComponents.register();
        EntityMemory.registerFunction(plugin, mem ->{
            if(!(mem instanceof PlayerMemory data)) return;
            data.getDataHolders().register(new EntityTickablesHolder(mem));
        });
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        plugin.registerListeners(new TickableListener());
    }
}
