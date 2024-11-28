package killercreepr.cruxenchants.core.enchant;

import killercreepr.cruxenchants.api.enchant.CruxWrappedEnchant;
import net.kyori.adventure.key.Key;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

public class SimpleWrappedEnchant implements CruxWrappedEnchant {
    protected final @NotNull Enchantment enchantment;
    public SimpleWrappedEnchant(@NotNull Enchantment enchantment) {
        this.enchantment = enchantment;
    }

    @Override
    public @NotNull Enchantment enchantment() {
        return enchantment;
    }

    @Override
    public @NotNull Key key() {
        return enchantment.key();
    }
}
