package killercreepr.crux.config.bukkit;

import killercreepr.crux.config.bukkit.data.GenericValue;
import killercreepr.crux.config.bukkit.data.PotionEffectValue;
import killercreepr.crux.config.bukkit.value.CfgValue;
import killercreepr.crux.config.common.annotations.Config;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Config
public class TestCfg extends Cfg {
    public final CfgValue<String> TEST = new CfgValue<>("path.man.dude", new GenericValue("ayo"));
    public final CfgValue<PotionEffect> WOAH = new CfgValue<>(new PotionEffectValue(new PotionEffect(PotionEffectType.GLOWING, 20, 20)));
    public TestCfg(@NotNull Plugin plugin, @NotNull String path) {
        super(plugin, path);
    }

    public TestCfg(@NotNull File file) {
        super(file);
    }

    public TestCfg(@NotNull CruxConfig cfg) {
        super(cfg);
    }
}
