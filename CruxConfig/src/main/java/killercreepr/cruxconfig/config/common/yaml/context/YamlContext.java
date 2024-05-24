package killercreepr.cruxconfig.config.common.yaml.context;

import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import org.jetbrains.annotations.NotNull;

public class YamlContext {
    protected final @NotNull YamlRegistry registry;
    public YamlContext(@NotNull YamlRegistry registry) {
        this.registry = registry;
    }

    public @NotNull YamlRegistry getRegistry() {
        return registry;
    }
}
