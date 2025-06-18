package killercreepr.crux.core.loot.item.pool;

import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.loot.functions.LootFunction;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.tags.container.TagContainer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListItemPoolObject extends SimpleItemLootPoolObject{
    protected final @Nullable Collection<DynamicItem> items;
    public ListItemPoolObject(int weight, float quality, @Nullable List<LootCondition> conditions,
                              @Nullable List<LootFunction<ItemStack>> lootFunctions, @Nullable Collection<DynamicItem> item) {
        super(weight, quality, conditions, lootFunctions, (ctx) ->{
            if(item == null) return List.of();
            Collection<ItemStack> items = new ArrayList<>();
            TextParserContext parserCtx = TextParserContext.builder()
                .tags(TagContainer.merged().hookAllWithPrefix(ctx.info()))
                .build();
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

    public @Nullable Collection<DynamicItem> getItems() {
        return items;
    }
}
