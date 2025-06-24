package killercreepr.crux.core.loot.item.functions;

import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.crux.api.item.CruxItem;
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

public class SetCruxComponentFunction extends SimpleLootFunction<ItemStack> implements ItemLootFunction {
    private final @NotNull Collection<TypedDataComponent<?>> cruxComponents;

    public SetCruxComponentFunction(@Nullable Collection<LootCondition> conditions, @NotNull Collection<TypedDataComponent<?>> cruxComponents) {
        super(conditions);
        this.cruxComponents = cruxComponents;
    }


    @Override
    public ItemStack accept(@Nullable ItemStack i, @NotNull LootContext context) {
        if(i==null) return i;
        CruxItem item = CruxItem.wrap(i);
        cruxComponents.forEach(item::set);
        return i;
    }
}
