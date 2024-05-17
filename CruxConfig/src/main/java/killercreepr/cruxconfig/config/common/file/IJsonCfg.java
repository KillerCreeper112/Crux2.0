package killercreepr.cruxconfig.config.common.file;

import killercreepr.cruxconfig.config.common.json.JsonRegistry;
import killercreepr.cruxconfig.config.common.value.ICfgValue;
import org.jetbrains.annotations.NotNull;

public interface IJsonCfg<T, V extends ICfgValue<?>> extends ICfg<T, V>{
    @NotNull
    JsonRegistry jsonRegistry();
}
