package killercreepr.cruxconfig.config.common.file;

import killercreepr.cruxconfig.config.common.json.registry.JsonRegistry;
import killercreepr.cruxconfig.config.common.value.IConfigValue;
import org.jetbrains.annotations.NotNull;

public interface IJsonCfg<T, V extends IConfigValue<?, ?>> extends ICfg<T, V>{
    @NotNull
    JsonRegistry jsonRegistry();
}
