package killercreepr.cruxenchants.api.enchant;

import net.kyori.adventure.key.Keyed;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

public interface CruxWrappedEnchant extends Keyed {
    @NotNull
    Enchantment enchantment();
}
