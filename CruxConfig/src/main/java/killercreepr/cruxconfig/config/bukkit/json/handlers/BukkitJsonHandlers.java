package killercreepr.cruxconfig.config.bukkit.json.handlers;

import killercreepr.cruxconfig.config.common.json.JsonRegistry;
import killercreepr.cruxconfig.config.common.json.container.GenericJsonHandler;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BukkitJsonHandlers {
    public static void init(@NotNull JsonRegistry registry){
        registry.registerContainerHandler(
                new GenericJsonHandler<>("vector", Vector.class),
                new GenericJsonHandler<>("uuid", UUID.class)
        );
    }
}
