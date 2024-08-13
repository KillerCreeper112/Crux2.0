package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.loot.api.LootPoolObject;
import killercreepr.crux.loot.api.conditions.LootCondition;
import killercreepr.crux.loot.api.functions.LootFunction;
import killercreepr.crux.loot.item.SimpleItemLootPool;
import killercreepr.crux.loot.item.api.ItemLootFunction;
import killercreepr.crux.loot.item.api.ItemLootPool;
import killercreepr.crux.loot.item.api.ItemLootPoolObject;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.bukkit.handler.FileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FileItemLootPool implements FileHandler<ItemLootPool> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull ItemLootPool object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable ItemLootPool deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        NumberProvider rolls = registry.deserialize(NumberProvider.class, o.get("rolls"));
        if(rolls == null) return null;

        int weight = o.getObject(Integer.class, "weight", 0);
        float quality = o.getObject(Float.class, "quality", 0f);
        List<LootCondition> conditions = registry.deserialize(
            new TypeToken<List<LootCondition>>(){}.getType(), o.get("conditions")
        );
        List<LootFunction<ItemStack>> functions = registry.deserialize(
            new TypeToken<List<ItemLootFunction>>(){}.getType(), o.get("functions")
        );
        List<LootPoolObject<ItemStack>> entries = registry.deserialize(
            new TypeToken<List<ItemLootPoolObject>>(){}.getType(), o.get("entries")
        );
        if(entries==null) return null;
        return new SimpleItemLootPool(
            weight, quality, conditions, functions, rolls, entries
        );
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "item_loot_pool";
    }
}
