package killercreepr.crux;

import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.item.dynamic.components.DynamicPersistentTag;
import killercreepr.crux.item.dynamic.components.TagContainerPersistTagHandler;
import killercreepr.crux.listener.EntitySpawnListener;
import killercreepr.crux.listener.PlayerDataListener;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.module.StandardModules;
import killercreepr.crux.plugin.CruxPlugin;
import org.jetbrains.annotations.NotNull;

public class CruxMainModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_MAIN;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    @Override
    public void onDisable(@NotNull CruxPlugin plugin) {
        for(EntityMemory data : EntityMemory.REGISTRY){
            data.removeDataHolders(data.value());
        }
    }

    @Override
    public void onLoad(@NotNull CruxPlugin plugin) {
        DynamicPersistentTag.HANDLERS.register("tag_container", new TagContainerPersistTagHandler());
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        plugin.registerListeners(
            new PlayerDataListener(),
            new EntitySpawnListener()
        );
        Crux.buildTickTask().runTaskTimerAsynchronously(plugin, 20L, 1L);
    }
}
