package killercreepr.cruxconfig.config.common.yaml;

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
