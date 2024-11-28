package killercreepr.cruxenchants.api.enchant;

import killercreepr.cruxenchants.core.enchant.SimpleEnchantWrapper;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CruxEnchantWrapper extends Keyed {
    static CruxEnchantWrapper enchantWrapper(@NotNull Key key){
        return new SimpleEnchantWrapper(key);
    }
    @Nullable CruxWrappedEnchant wrap(@NotNull Enchantment ench);
}
