package killercreepr.cruxpotion.config;

import killercreepr.cruxconfig.config.bukkit.file.Cfg;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.value.CfgValue;
import killercreepr.cruxconfig.config.bukkit.value.CommonValue;
import killercreepr.cruxconfig.config.bukkit.value.NotNullValue;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Config extends Cfg {
    public final NotNullValue<Boolean> REMOVE_POTIONS_UPON_DEATH = new NotNullValue<>(true){};
    public final NotNullValue<Boolean> SAVE_POTIONS_UPON_QUIT = new NotNullValue<>(true){};
    /*public final NumCfgValue ITEM_POTION_LORE_PRIORITY = new NumCfgValue(1);
    public final CfgValue<Map<PotionEffectType.Category, String>> ITEM_POTION_COLORS = new CfgValue<>(new PotionCategoryStringMapValue(),
            "Determines the format for each potion category.",
            "You can use: " + Arrays.toString(PotionEffectType.Category.values()));*/

    public final CfgValue<Map<NamespacedKey, PotionEffectType.Category>> POTION_CATEGORIES = new CommonValue<>(new HashMap<>(){{
        put(new NamespacedKey("namespace", "key"), PotionEffectType.Category.HARMFUL);
    }}){};

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
