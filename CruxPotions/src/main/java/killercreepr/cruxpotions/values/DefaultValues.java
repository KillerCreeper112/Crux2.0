package killercreepr.cruxpotions.values;

import killercreepr.crux.data.Holder;
import net.kyori.adventure.key.Key;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultValues implements ValuesProvider{
    public static final Holder<List<String>> ENCHANTS_TAG_FORMAT = Holder.direct(List.of(
        "<gray><enchant_name> <enchant_level>"
    ));
    public static final Holder<Boolean> REMOVE_POTIONS_UPON_DEATH = Holder.direct(true);
    public static final Holder<Boolean> SAVE_POTIONS_UPON_QUIT = Holder.direct(true);
    public static final Holder<Map<Key, PotionEffectType.Category>> POTION_CATEGORIES = Holder.direct(new HashMap<>(){{
        put(Key.key("namespace", "key"), PotionEffectType.Category.HARMFUL);
    }});

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
}
