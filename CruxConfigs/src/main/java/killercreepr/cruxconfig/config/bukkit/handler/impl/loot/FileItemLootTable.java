package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.loot.LootPool;
import killercreepr.crux.api.loot.item.ItemLootPool;
import killercreepr.crux.api.loot.item.ItemLootTable;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.loot.item.SimpleItemLootTable;
import killercreepr.crux.core.registries.CruxRegistries;
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
        if(!(e instanceof FileObject o)){
            String s = ctx.getRegistry().deserializeFromFile(String.class, e);
            if(s == null) return null;
            if(s.startsWith("#")) s = s.substring(1);
            var item = (ItemLootTable) CruxRegistries.ITEM_LOOT_TABLE.get(Crux.key(s));
            if(item == null) Crux.logError(s + " item loot table not found!");
            return item;
        }
        FileRegistry registry = ctx.getRegistry();
        Key key = registry.deserializeFromFile(Key.class, o.get("key"));
        if(key==null) return null;
        return deserializeFromFile(ctx, o, key);
    }

    public @Nullable ItemLootTable deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull Key key) {
        FileRegistry registry = ctx.getRegistry();
        NumberProvider rolls = registry.deserializeFromFile(NumberProvider.class, e.get("rolls"));
        if(rolls == null) rolls = NumberProvider.constant(1);
        List<LootPool<ItemStack>> pools = registry.deserializeFromFile(
            new TypeToken<List<ItemLootPool>>(){}.getType(), e.get("pools")
        );
        return new SimpleItemLootTable(
            key, rolls, pools
        );
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "item_loot_table";
    }
}
