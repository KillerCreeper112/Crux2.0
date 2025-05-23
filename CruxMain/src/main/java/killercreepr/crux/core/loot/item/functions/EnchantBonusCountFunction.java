package killercreepr.crux.core.loot.item.functions;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import killercreepr.crux.api.enchantment.DropFormula;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.loot.item.ItemLootFunction;
import killercreepr.crux.core.loot.functions.SimpleLootFunction;
import net.kyori.adventure.key.Key;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class EnchantBonusCountFunction extends SimpleLootFunction<ItemStack> implements ItemLootFunction {
    protected final String target;
    protected final @NotNull DropFormula formula;
    protected final Key enchant;
    protected final Collection<EquipmentSlot> slots;

    public EnchantBonusCountFunction(@Nullable Collection<LootCondition> conditions, String target, @NotNull DropFormula formula, Key enchant, Collection<EquipmentSlot> slots) {
        super(conditions);
        this.target = target;
        this.formula = formula;
        this.enchant = enchant;
        this.slots = slots;
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

    @Override
    public ItemStack accept(@Nullable ItemStack i, @NotNull LootContext context) {
        if(i==null) return i;
        Enchantment ench = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(enchant);
        if(ench == null) return i;

        int level;
        if (context.info().get(target) instanceof LivingEntity livingEntity) {
            level = getEnchantLevel(livingEntity, ench);
        }else if(context.info().get(target) instanceof ItemStack item){
            level = item.getEnchantmentLevel(ench);
        }else if(context.info().get(target) instanceof Item item) {
            level = item.getItemStack().getEnchantmentLevel(ench);
        }else return i;

        if (level == 0) {
            return i;
        }
        i.setAmount(formula.calculateNewCount(context.getRandom(), i.getAmount(), level));
        return i;
    }
}
