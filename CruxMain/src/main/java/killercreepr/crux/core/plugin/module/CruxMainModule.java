package killercreepr.crux.core.plugin.module;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.plugin.module.CruxModule;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.command.CruxLootCommands;
import killercreepr.crux.core.component.CruxComponents;
import killercreepr.crux.core.listener.EntitySpawnListener;
import killercreepr.crux.core.listener.LootContainerListener;
import killercreepr.crux.core.listener.PlayerDataListener;
import killercreepr.crux.core.plugin.CruxPlugin;
import org.jetbrains.annotations.NotNull;

public class CruxMainModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_MAIN;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    static {
        //ComponentParserTypes.register();
    }

    @Override
    public void onLoad(@NotNull CruxPlugin plugin) {
        CruxComponents.register();
        CruxLootCommands.register(plugin);
    }

    @Override
    public void onDisable(@NotNull CruxPlugin plugin) {
        for(EntityMemory data : EntityMemory.REGISTRY){
            data.removeDataHolders(data.value());
        }
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        plugin.registerListeners(
            new PlayerDataListener(),
            new EntitySpawnListener(),
            new LootContainerListener()
        );
        Crux.buildTickTask().runTaskTimerAsynchronously(plugin, 20L, 1L);
    }
}
