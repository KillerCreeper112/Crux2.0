package killercreepr.cruxconfig.config.common.json.context;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.json.registry.JsonRegistry;
import org.jetbrains.annotations.NotNull;

public class JsonContext extends FileContext<JsonRegistry> {
    public JsonContext(@NotNull JsonRegistry registry) {
        super(registry);
    }
}
