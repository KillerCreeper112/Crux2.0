package killercreepr.crux.core.loot.conditions;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import killercreepr.crux.api.enchantment.CruxLevelBasedValue;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.conditions.LootCondition;
import net.kyori.adventure.key.Key;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class RandomChanceEnchantedBonusCondition implements LootCondition {
    protected final String target;
    protected final CruxLevelBasedValue enchantedChance;
    protected final float unenchantedChance;
    protected final Key enchant;
    protected final Collection<EquipmentSlot> slots;

    public RandomChanceEnchantedBonusCondition(String target, CruxLevelBasedValue enchantedChance, float unenchantedChance, Key enchant,
                                               Collection<EquipmentSlot> slots) {
        this.target = target;
        this.enchantedChance = enchantedChance;
        this.unenchantedChance = unenchantedChance;
        this.enchant = enchant;
        this.slots = slots;
    }

    @Override
    public @NotNull String getTarget() {
        return target;
    }

    public int getEnchantLevel(LivingEntity e, Enchantment ench){
        var equip = e.getEquipment();
        if(equip == null) return 0;
        int level = 0;
        for(var slot : slots){
            if(!e.canUseEquipmentSlot(slot)) continue;
            ItemStack item = equip.getItem(slot);
            if(CruxItem.isEmpty(item)) continue;
            level += item.getEnchantmentLevel(ench);
        }
        return level;
    }

    public Collection<EquipmentSlot> getSlots() {
        return slots;
    }

    public CruxLevelBasedValue getEnchantedChance() {
        return enchantedChance;
    }

    public float getUnenchantedChance() {
        return unenchantedChance;
    }

    public Key getEnchant() {
        return enchant;
    }

    @Override
    public boolean test(@NotNull LootContext context) {
        var entity = context.info().get(target);
        int level;
        if (entity instanceof LivingEntity livingEntity) {
            Enchantment ench = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(enchant);
            if(ench != null) level = getEnchantLevel(livingEntity, ench);
            else level = 0;
        } else {
            level = 0;
        }

        float f = level > 0 ? this.enchantedChance.calculate(level) : this.unenchantedChance;
        return context.getRandom().nextFloat() < f;
    }
}
