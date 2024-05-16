package killercreepr.sometests;

import killercreepr.crux.config.bukkit.file.Cfg;
import killercreepr.crux.config.bukkit.file.CruxConfig;
import killercreepr.crux.config.bukkit.data.GenericValue;
import killercreepr.crux.config.bukkit.value.CfgValue;
import killercreepr.crux.config.common.annotations.Config;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Config
public class TestCf extends Cfg {
    public final CfgValue<String> AYO = new CfgValue<>(new GenericValue("test again hello everyone"));
    public TestCf(@NotNull Plugin plugin, @NotNull String path) {
        super(plugin, path);
    }

    public TestCf(@NotNull File file) {
        super(file);
    }

    public TestCf(@NotNull CruxConfig cfg) {
        super(cfg);
    }
}
