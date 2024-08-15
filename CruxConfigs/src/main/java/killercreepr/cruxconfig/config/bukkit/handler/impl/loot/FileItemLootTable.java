package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.loot.api.LootPool;
import killercreepr.crux.loot.item.SimpleItemLootTable;
import killercreepr.crux.loot.item.api.ItemLootPool;
import killercreepr.crux.loot.item.api.ItemLootTable;
import killercreepr.crux.util.CruxObjects;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FileItemLootTable implements FileObjectHandler<ItemLootTable> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull ItemLootTable object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable ItemLootTable deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        Key key = registry.deserializeFromFile(Key.class, o.get("key"));
        if(key==null) return null;
        return deserializeFromFile(ctx, o, key);
    }

    public @Nullable ItemLootTable deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull Key key) {
        FileRegistry registry = ctx.getRegistry();
        NumberProvider rolls = registry.deserializeFromFile(NumberProvider.class, e.get("rolls"));
        List<LootPool<ItemStack>> pools = registry.deserializeFromFile(
            new TypeToken<List<ItemLootPool>>(){}.getType(), e.get("pools")
        );
        if(CruxObjects.checkNull(rolls, pools)) return null;
        return new SimpleItemLootTable(
            key, rolls, pools
        );
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "item_loot_table";
    }
}
