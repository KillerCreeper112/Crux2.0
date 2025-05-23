package killercreepr.crux.core.loot.conditions;

import killercreepr.crux.api.enchantment.CruxLevelBasedValue;
import killercreepr.crux.api.loot.LootContext;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Collection;

public class RandomLuckChanceEnchantedBonusCondition extends RandomChanceEnchantedBonusCondition {
    private final float luckMultiplier;
    public RandomLuckChanceEnchantedBonusCondition(String target, CruxLevelBasedValue enchantedChance, float unenchantedChance, Key enchant, Collection<EquipmentSlot> slots, float luckMultiplier) {
        super(target, enchantedChance, unenchantedChance, enchant, slots);
        this.luckMultiplier = luckMultiplier;
    }

    @Override
    public float getAddonChance(LootContext ctx) {
        return ctx.getLuck() * luckMultiplier;
    }

    public float getLuckMultiplier() {
        return luckMultiplier;
    }
}
