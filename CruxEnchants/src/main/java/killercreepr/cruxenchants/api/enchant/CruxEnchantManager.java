package killercreepr.cruxenchants.api.enchant;

import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CruxEnchantManager {
    @Nullable CruxWrappedEnchant wrap(@NotNull Enchantment enchantment);
}
