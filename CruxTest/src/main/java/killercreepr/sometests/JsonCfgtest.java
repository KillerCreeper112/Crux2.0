package killercreepr.sometests;

import killercreepr.crux.config.bukkit.data.GenericValue;
import killercreepr.crux.config.bukkit.file.CruxJson;
import killercreepr.crux.config.bukkit.file.JsonCfg;
import killercreepr.crux.config.bukkit.value.CfgValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class JsonCfgtest extends JsonCfg {
    public final CfgValue<String> STRING_MAN = new CfgValue<>(new GenericValue("test my man"));
    public JsonCfgtest(@NotNull Plugin plugin, @NotNull String path) {
        super(plugin, path);
    }

    public JsonCfgtest(@NotNull File file) {
        super(file);
    }

    public JsonCfgtest(@NotNull CruxJson cfg) {
        super(cfg);
    }
}
