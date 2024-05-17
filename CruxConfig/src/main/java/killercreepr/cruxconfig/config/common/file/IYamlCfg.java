package killercreepr.cruxconfig.config.common.file;

import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import org.jetbrains.annotations.NotNull;

public interface IYamlCfg<T> extends ICruxConfig<T>{
    @NotNull
    YamlRegistry yamlRegistry();
}
