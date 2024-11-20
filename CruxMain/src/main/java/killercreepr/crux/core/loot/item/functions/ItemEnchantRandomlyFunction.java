package killercreepr.crux.core.loot.item.functions;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.loot.item.ItemLootFunction;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.loot.functions.SimpleLootFunction;
import killercreepr.crux.core.util.CruxCollection;
import killercreepr.crux.core.util.CruxMath;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class ItemEnchantRandomlyFunction extends SimpleLootFunction<ItemStack> implements ItemLootFunction {
    private final @NotNull NumberProvider rolls;
    private final boolean distinct;
    public ItemEnchantRandomlyFunction(@Nullable Collection<LootCondition> conditions, @NotNull NumberProvider rolls, boolean distinct) {
        super(conditions);
        this.rolls = rolls;
        this.distinct = distinct;
    }

    @Override
    public ItemStack accept(@Nullable ItemStack i, @NotNull LootContext ctx) {
        if(i == null) return null;

        int roll = rolls.sample(ctx.getRandom()).intValue();
        while(roll > 0){
            roll--;
            i = roll(i, ctx);
        }
        return i;
    }

    public ItemStack roll(ItemStack i, LootContext ctx){
        Enchantment enchant = findValidEnchant(i);
        if(enchant == null) return null;
        int level = CruxMath.random(enchant.getStartLevel(), enchant.getMaxLevel(), ctx.getRandom());
        if(i.getType() == Material.BOOK){
            i = i.withType(Material.ENCHANTED_BOOK);
        }
        if(i.getType() == Material.ENCHANTED_BOOK){
            i.editMeta(EnchantmentStorageMeta.class, meta ->{
                meta.addStoredEnchant(enchant, level, true);
            });
            return i;
        }
        i.editMeta(meta ->{
            meta.addEnchant(enchant, level, true);
        });
        return i;
    }

    public Collection<Enchantment> getEnchants(ItemStack item){
        if(item.getItemMeta() instanceof EnchantmentStorageMeta m){
            return m.getStoredEnchants().keySet();
        }
        return item.getEnchantments().keySet();
    }

    public Enchantment findValidEnchant(ItemStack item){
        Collection<Enchantment> alreadyApplied = getEnchants(item);
        Collection<Enchantment> list = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).stream()
            .filter(ench ->{
                if(distinct){
                    if(alreadyApplied.contains(ench)) return false;
                }

                if(item.getType() == Material.BOOK) return true;
                if(!ench.canEnchantItem(item)) return false;
                for(Enchantment applied : alreadyApplied){
                    if(ench.conflictsWith(applied)) return false;
                }
                return true;
            })
            .toList();
        return CruxCollection.getRandom(list);
    }
}
