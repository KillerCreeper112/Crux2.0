package killercreepr.cruxconfig.config.common.yaml.context;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import org.jetbrains.annotations.NotNull;

public class YamlContext extends FileContext<YamlRegistry> {
    public YamlContext(@NotNull YamlRegistry registry) {
        super(registry);
    }
}
