package killercreepr.crux.core.loot.item.functions;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.loot.item.ItemLootFunction;
import killercreepr.crux.core.loot.functions.SimpleLootFunction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class SetBasePotionFunction extends SimpleLootFunction<ItemStack> implements ItemLootFunction {
    private final @NotNull PotionType potionType;

    public SetBasePotionFunction(@Nullable Collection<LootCondition> conditions, @NotNull PotionType potionType) {
        super(conditions);
        this.potionType = potionType;
    }


    @Override
    public ItemStack accept(@Nullable ItemStack i, @NotNull LootContext context) {
        if(i==null) return i;
        i.editMeta(PotionMeta.class, meta ->{
            meta.setBasePotionType(potionType);
        });
        return i;
    }
}
