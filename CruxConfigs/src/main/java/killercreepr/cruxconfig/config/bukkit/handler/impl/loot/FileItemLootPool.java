package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.loot.LootPoolObject;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.loot.functions.LootFunction;
import killercreepr.crux.api.loot.item.ItemLootFunction;
import killercreepr.crux.api.loot.item.ItemLootPool;
import killercreepr.crux.api.loot.item.ItemLootPoolObject;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.loot.item.SimpleItemLootPool;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FileItemLootPool implements FileObjectHandler<ItemLootPool> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull ItemLootPool object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable ItemLootPool deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        NumberProvider rolls = registry.deserializeFromFile(NumberProvider.class, o.get("rolls"));
        if(rolls == null) rolls = NumberProvider.constant(1);

        List<LootCondition> conditions = registry.deserializeFromFile(
            new TypeToken<List<LootCondition>>(){}.getType(), o.get("conditions")
        );
        List<LootFunction<ItemStack>> functions = registry.deserializeFromFile(
            new TypeToken<List<ItemLootFunction>>(){}.getType(), o.get("functions")
        );
        List<LootPoolObject<ItemStack>> entries = registry.deserializeFromFile(
            new TypeToken<List<ItemLootPoolObject>>(){}.getType(), o.get("entries")
        );
        if(entries==null) return null;

        Integer entriesDupeCount = o.getObject(Integer.class,"entries_dupe_count", 0);
        if(entriesDupeCount > 0){
            List<LootPoolObject<ItemStack>> copy = new ArrayList<>(entries);
            for(int i = 0; i < entriesDupeCount; i++){
                entries.addAll(copy);
            }
        }

        boolean allowDuplicates = o.getOrDefaultObject(Boolean.class, "allow_duplicates",false);
        return new SimpleItemLootPool(
            conditions, functions, rolls, entries, allowDuplicates
        );
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "item_loot_pool";
    }
}
