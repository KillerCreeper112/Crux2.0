package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.loot.conditions.LootCondition;
import killercreepr.crux.loot.functions.LootFunction;
import killercreepr.crux.loot.impl.item.SimpleItemLootObject;
import killercreepr.crux.loot.item.ItemLootFunction;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FileSimpleLootObject implements FileObjectHandler<SimpleItemLootObject> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull SimpleItemLootObject object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable SimpleItemLootObject deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        int weight = o.getObject(Integer.class, "weight", 0);
        float quality = o.getObject(Float.class, "quality", 0f);

        List<LootCondition> conditions = registry.deserializeFromFile(
            new TypeToken<List<LootCondition>>(){}.getType(), o.get("conditions")
        );
        List<LootFunction<ItemStack>> functions = registry.deserializeFromFile(
            new TypeToken<List<ItemLootFunction>>(){}.getType(), o.get("functions")
        );
        return new SimpleItemLootObject(
            weight, quality, conditions, functions
        );
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "";
    }
}
