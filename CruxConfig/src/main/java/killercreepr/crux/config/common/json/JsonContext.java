package killercreepr.crux.config.common.json;

import org.jetbrains.annotations.NotNull;

public class JsonContext {
    protected final @NotNull JsonRegistry registry;
    public JsonContext(@NotNull JsonRegistry registry) {
        this.registry = registry;
    }

    public @NotNull JsonRegistry getRegistry() {
        return registry;
    }
}
