package killercreepr.crux.core.loot.item.functions;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.loot.item.ItemLootFunction;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.loot.functions.SimpleLootFunction;
import net.kyori.adventure.key.Key;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Random;

public class EnchantedCountFunction extends SimpleLootFunction<ItemStack> implements ItemLootFunction {
    protected final String target;
    private final @NotNull NumberProvider amount;
    private final boolean add;
    protected final Key enchant;
    protected final Collection<EquipmentSlot> slots;
    protected final NumberProvider max;
    public EnchantedCountFunction(@Nullable Collection<LootCondition> conditions, String target, @NotNull NumberProvider amount, boolean add, Key enchant, Collection<EquipmentSlot> slots, NumberProvider max) {
        super(conditions);
        this.target = target;
        this.amount = amount;
        this.add = add;
        this.enchant = enchant;
        this.slots = slots;
        this.max = max;
    }


    public int getEnchantLevel(org.bukkit.entity.LivingEntity e, Enchantment ench){
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

    @Override
    public ItemStack accept(@Nullable ItemStack i, @NotNull LootContext context) {
        if(i==null) return i;
        if (context.info().get(target) instanceof LivingEntity livingEntity) {
            Enchantment ench = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(enchant);
            if(ench == null) return i;
            int level = getEnchantLevel(livingEntity, ench);
            if (level == 0) {
                return i;
            }

            Random source = context.getRandom();
            int amount = this.amount.sample(source).intValue();

            int add = Math.round((float)level * amount);

            if(max != null){
                int max = this.max.sample(source).intValue();
                if(add > max) add = max;
            }
            i.setAmount(this.add ? (i.getAmount() + add) : add);
        }
        return i;
    }
}
