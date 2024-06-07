package killercreepr.cruxpotions.config;

import killercreepr.crux.data.Holder;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.cruxconfig.config.bukkit.file.Cfg;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.value.CfgValue;
import killercreepr.cruxconfig.config.bukkit.value.CommonValue;
import killercreepr.cruxconfig.config.bukkit.value.NotNullValue;
import killercreepr.cruxpotions.values.DefaultValues;
import killercreepr.cruxpotions.values.ValuesProvider;
import net.kyori.adventure.key.Key;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Map;

public class Config extends Cfg implements ValuesProvider {
    public final NotNullValue<Boolean> REMOVE_POTIONS_UPON_DEATH = new NotNullValue<>(DefaultValues.REMOVE_POTIONS_UPON_DEATH.value()){};
    public final NotNullValue<Boolean> SAVE_POTIONS_UPON_QUIT = new NotNullValue<>(DefaultValues.SAVE_POTIONS_UPON_QUIT.value()){};
    /*public final NumCfgValue ITEM_POTION_LORE_PRIORITY = new NumCfgValue(1);
    public final CfgValue<Map<PotionEffectType.Category, String>> ITEM_POTION_COLORS = new CfgValue<>(new PotionCategoryStringMapValue(),
            "Determines the format for each potion category.",
            "You can use: " + Arrays.toString(PotionEffectType.Category.values()));*/

    public final CfgValue<Map<Key, PotionEffectType.Category>> POTION_CATEGORIES = new CommonValue<>(DefaultValues.POTION_CATEGORIES.value()){};

    public Config(@NotNull Plugin plugin, @NotNull String path) {
        super(plugin, path);
    }

    public Config(@NotNull File file) {
        super(file);
    }

    public Config(@NotNull CruxConfig cfg) {
        super(cfg);
    }

    @Override
    public @NotNull Holder<Boolean> removePotionsUponDeath() {
        return REMOVE_POTIONS_UPON_DEATH;
    }

    @Override
    public @NotNull Holder<Boolean> savePotionsUponQuit() {
        return SAVE_POTIONS_UPON_QUIT;
    }

    @Override
    public @NotNull Holder<Map<Key, PotionEffectType.Category>> potionCategories() {
        return POTION_CATEGORIES;
    }

    @Override
    public void reload(@NotNull CruxPlugin plugin) {
        setup();
    }
}
