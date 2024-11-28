package killercreepr.cruxenchants.core.enchant;

import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.cruxenchants.api.enchant.CruxEnchantManager;
import killercreepr.cruxenchants.api.enchant.CruxEnchantWrapper;
import killercreepr.cruxenchants.api.enchant.CruxWrappedEnchant;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleEnchantManager implements CruxEnchantManager {
    protected final @NotNull KeyedRegistry<CruxEnchantWrapper> registry;

    public SimpleEnchantManager(@NotNull KeyedRegistry<CruxEnchantWrapper> registry) {
        this.registry = registry;
    }

    @Override
    public @Nullable CruxWrappedEnchant wrap(@NotNull Enchantment enchantment) {
        CruxEnchantWrapper wrapper = registry.get(enchantment.key());
        if(wrapper == null) return null;
        return wrapper.wrap(enchantment);
    }
}
