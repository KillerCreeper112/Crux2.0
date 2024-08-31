package killercreepr.crux.loot.impl.item.pool;

import killercreepr.crux.data.Holder;
import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.LootPoolObject;
import killercreepr.crux.loot.conditions.LootCondition;
import killercreepr.crux.loot.functions.LootFunction;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class AlternativeItemPoolObject extends SimpleItemLootPoolObject{
    protected final @NotNull List<LootPoolObject<ItemStack>> items;
    public AlternativeItemPoolObject(int weight, float quality, @Nullable List<LootCondition> conditions,
                                     @Nullable List<LootFunction<ItemStack>> lootFunctions, @NotNull List<LootPoolObject<ItemStack>> items) {
        super(weight, quality, conditions, lootFunctions, null);
        this.items = items;
    }

    public AlternativeItemPoolObject(int weight, float quality,
                                     @Nullable List<LootFunction<ItemStack>> lootFunctions,
                                     @NotNull List<LootPoolObject<ItemStack>> item) {
        this(weight, quality, null, lootFunctions, item);
    }

    public AlternativeItemPoolObject(int weight, float quality, @NotNull List<LootPoolObject<ItemStack>> item) {
        this(weight, quality, null, item);
    }

    public @Nullable Holder<Collection<LootPoolObject<ItemStack>>> getLootObjects(@NotNull LootContext ctx) {
        for(LootPoolObject<ItemStack> item : items){
            if(item.testConditions(ctx)) return Holder.direct(Set.of(item));
        }
        return null;
    }

    @Override
    public @Nullable Holder<Collection<ItemStack>> getItems(@NotNull LootContext ctx) {
        Holder<Collection<LootPoolObject<ItemStack>>> holder = getLootObjects(ctx);
        if(holder==null) return null;
        Collection<LootPoolObject<ItemStack>> collection = holder.value();
        if(collection==null) return null;

        Collection<ItemStack> items = new ArrayList<>();
        for(LootPoolObject<ItemStack> item : collection){
            Holder<Collection<ItemStack>> gotItems = item.getItems(ctx);
            if(gotItems==null) continue;
            Collection<ItemStack> result = gotItems.value();
            if(result==null) continue;
            items.addAll(result);
        }
        return Holder.direct(items);
    }
}
