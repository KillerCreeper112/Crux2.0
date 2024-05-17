package killercreepr.cruxconfig.config.common.yaml;

import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import org.jetbrains.annotations.NotNull;

public interface YamlSerializable {
    @NotNull
    YamlElement serializeToYaml(@NotNull YamlContext context);
}
