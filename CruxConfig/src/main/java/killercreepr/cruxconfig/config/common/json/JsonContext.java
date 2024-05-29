package killercreepr.cruxconfig.config.common.json;

import killercreepr.cruxconfig.config.common.FileContext;
import org.jetbrains.annotations.NotNull;

public class JsonContext extends FileContext<JsonRegistry> {
    public JsonContext(@NotNull JsonRegistry registry) {
        super(registry);
    }
}
