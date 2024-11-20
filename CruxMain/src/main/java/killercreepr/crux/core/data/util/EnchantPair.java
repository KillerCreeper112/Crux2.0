package killercreepr.crux.core.data.util;

import org.bukkit.enchantments.Enchantment;

public class EnchantPair extends Pair<Enchantment, Integer> {
    public EnchantPair(Enchantment first, Integer second) {
        super(first, second);
    }

    public Enchantment getEnchant(){
        return getFirst();
    }

    public int getLevel(){
        return getSecond();
    }
}
