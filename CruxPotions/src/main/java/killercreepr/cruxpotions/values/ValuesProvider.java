package killercreepr.cruxpotions.values;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.data.Reloadable;
import net.kyori.adventure.key.Key;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface ValuesProvider extends Reloadable {
    @NotNull Holder<List<String>> potionsFormat();
    @NotNull Holder<Boolean> removePotionsUponDeath();
    @NotNull Holder<Boolean> savePotionsUponQuit();
    @NotNull Holder<Map<Key, PotionEffectType.Category>> potionCategories();
}
