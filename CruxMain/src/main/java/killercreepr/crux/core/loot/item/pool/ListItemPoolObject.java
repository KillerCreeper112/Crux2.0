package killercreepr.crux.core.loot.item.pool;

import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.loot.functions.LootFunction;
import killercreepr.crux.api.text.context.TextParserContext;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListItemPoolObject extends SimpleItemLootPoolObject{
    protected final @NotNull Collection<DynamicItem> items;
    public ListItemPoolObject(int weight, float quality, @Nullable List<LootCondition> conditions,
                              @Nullable List<LootFunction<ItemStack>> lootFunctions, @NotNull Collection<DynamicItem> item) {
        super(weight, quality, conditions, lootFunctions, (ctx) ->{
            Collection<ItemStack> items = new ArrayList<>();
            TextParserContext parserCtx = TextParserContext.empty();
            for(DynamicItem dynamicItem : item){
                ItemStack i = dynamicItem.buildItem(parserCtx);
                if(i == null) continue;
                items.add(i);
            }
            return items;
        });
        this.items = item;
    }

    public ListItemPoolObject(int weight, float quality,
                              @Nullable List<LootFunction<ItemStack>> lootFunctions,
                              @NotNull Collection<DynamicItem> item) {
        this(weight, quality, null, lootFunctions, item);
    }

    public ListItemPoolObject(int weight, float quality, @NotNull Collection<DynamicItem> item) {
        this(weight, quality, null, item);
    }

    public @NotNull Collection<DynamicItem> getItems() {
        return items;
    }
}
