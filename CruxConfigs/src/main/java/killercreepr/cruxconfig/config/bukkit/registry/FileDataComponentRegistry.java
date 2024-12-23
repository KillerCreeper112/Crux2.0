package killercreepr.cruxconfig.config.bukkit.registry;

import killercreepr.crux.api.registry.MappedRegistry;
import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.bukkit.handler.impl.component.FileDataComponentType;
import net.kyori.adventure.key.Key;

public interface FileDataComponentRegistry extends MappedRegistry<Key, FileDataComponentType<?>> {
    default <T extends FileDataComponentType<?>> T register(String id, T type){
        return register(Crux.key(id), type);
    }
}
