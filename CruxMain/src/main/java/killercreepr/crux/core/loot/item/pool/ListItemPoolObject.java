package killercreepr.crux.core.loot.item.pool;

import killercreepr.crux.core.Crux;
import killercreepr.crux.api.handler.ItemHandler;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.loot.functions.LootFunction;
import killercreepr.crux.paper.ItemHolder;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListItemPoolObject extends SimpleItemLootPoolObject{
    protected final @NotNull Collection<Key> itemKeys;
    public ListItemPoolObject(int weight, float quality, @Nullable List<LootCondition> conditions,
                              @Nullable List<LootFunction<ItemStack>> lootFunctions, @NotNull Collection<Key> item) {
        super(weight, quality, conditions, lootFunctions, () ->{
            Collection<ItemStack> items = new ArrayList<>();
            ItemHandler handler = Crux.handlers().item();
            for(Key k : item){
                ItemHolder i = handler.getItem(k);
                if(i==null) continue;
                items.add(i.value());
            }
            return items;
        });
        this.itemKeys = item;
    }

    public ListItemPoolObject(int weight, float quality,
                              @Nullable List<LootFunction<ItemStack>> lootFunctions,
                              @NotNull Collection<Key> item) {
        this(weight, quality, null, lootFunctions, item);
    }

    public ListItemPoolObject(int weight, float quality, @NotNull Collection<Key> item) {
        this(weight, quality, null, item);
    }

    public @NotNull Collection<Key> getItemKeys() {
        return itemKeys;
    }
}
