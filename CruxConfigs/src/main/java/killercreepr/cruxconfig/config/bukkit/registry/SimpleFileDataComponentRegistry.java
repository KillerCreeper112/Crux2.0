package killercreepr.cruxconfig.config.bukkit.registry;

import killercreepr.crux.core.registry.SimpleMappedRegistry;
import killercreepr.cruxconfig.config.bukkit.handler.impl.component.FileDataComponentType;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SimpleFileDataComponentRegistry extends SimpleMappedRegistry<Key, FileDataComponentType<?>> implements FileDataComponentRegistry{
    public SimpleFileDataComponentRegistry(@NotNull Map<Key, FileDataComponentType<?>> map) {
        super(map);
    }

    public SimpleFileDataComponentRegistry() {
    }
}
