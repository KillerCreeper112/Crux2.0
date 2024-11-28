package killercreepr.cruxenchants.core.enchant;

import killercreepr.cruxenchants.api.enchant.CruxEnchantWrapper;
import killercreepr.cruxenchants.api.enchant.CruxWrappedEnchant;
import net.kyori.adventure.key.Key;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleEnchantWrapper implements CruxEnchantWrapper {
    protected final @NotNull Key key;
    public SimpleEnchantWrapper(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public @Nullable CruxWrappedEnchant wrap(@NotNull Enchantment ench) {
        return new SimpleWrappedEnchant(ench);
    }
    
    @Override
    public @NotNull Key key() {
        return key;
    }
}
