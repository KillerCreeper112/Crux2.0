package killercreepr.sometests;

import killercreepr.cruxconfig.config.bukkit.data.PotionEffectListValue;
import killercreepr.cruxconfig.config.bukkit.file.Cfg;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.value.CfgValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class Config extends Cfg {
    public final CfgValue<Collection<PotionEffect>> SWAP_HAND_EFFECTS = new CfgValue<>(new PotionEffectListValue(List.of(
            new PotionEffect(PotionEffectType.SPEED, 300, 0),
            new PotionEffect(PotionEffectType.GLOWING, 300, 0)
    )));
    public Config(@NotNull Plugin plugin, @NotNull String path) {
        super(plugin, path);
    }

    public Config(@NotNull File file) {
        super(file);
    }

    public Config(@NotNull CruxConfig cfg) {
        super(cfg);
    }
}
