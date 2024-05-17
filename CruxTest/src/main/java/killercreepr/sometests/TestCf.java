package killercreepr.sometests;

import killercreepr.cruxconfig.config.bukkit.data.GenericValue;
import killercreepr.cruxconfig.config.bukkit.file.Cfg;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.value.CfgValue;
import killercreepr.cruxconfig.config.common.annotations.Config;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Config
public class TestCf extends Cfg {
    public final CfgValue<String> AYO = new CfgValue<>(new GenericValue("test again hello everyone"));
    public final CfgValue<Vector> VECTOR = new CfgValue<>(new GenericValue(new Vector(0.3, 12, -2)));
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
