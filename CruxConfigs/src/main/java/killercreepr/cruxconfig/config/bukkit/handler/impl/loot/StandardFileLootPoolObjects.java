package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.crux.api.loot.LootPoolObject;
import killercreepr.crux.api.loot.LootTable;
import killercreepr.crux.api.loot.item.ItemLootPoolObject;
import killercreepr.crux.api.loot.item.ItemLootTable;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.loot.item.SimpleItemLootObject;
import killercreepr.crux.core.loot.item.pool.AlternativeItemPoolObject;
import killercreepr.crux.core.loot.item.pool.ListItemPoolObject;
import killercreepr.crux.core.loot.item.pool.LootTableItemPoolObject;
import killercreepr.crux.core.loot.item.pool.LootTableReferenceItemPoolObject;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileObject;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class StandardFileLootPoolObjects {
    public static void register(@NotNull FileItemLootPoolObject file){
        file.registerCustomHandler(new SimpleFileItemLootPoolObject<>(Crux.key("item")) {
            @Override
            public @Nullable ItemLootPoolObject deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                FileRegistry registry = ctx.getRegistry();
                SimpleItemLootObject loot = registry.deserializeFromFile(SimpleItemLootObject.class, e);
                if(loot==null) return null;

                Collection<DynamicItem> items = registry.deserializeFromFile(
                    new TypeToken<Collection<DynamicItem>>(){}.getType(),
                    e.get("item")
                );
                if(items == null){
                    items =registry.deserializeFromFile(
                        new TypeToken<Collection<DynamicItem>>(){}.getType(),
                        e.get("items")
                    );
                }

                return new ListItemPoolObject(
                    loot.getWeight(), loot.getQuality(), loot.getConditions(), loot.getFunctions(), items
                );
            }
        });
        file.registerCustomHandler(new SimpleFileItemLootPoolObject<>(Crux.key("loot_table")) {
            @Override
            public @Nullable ItemLootPoolObject deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                FileRegistry registry = ctx.getRegistry();
                SimpleItemLootObject loot = registry.deserializeFromFile(SimpleItemLootObject.class, e);
                if(loot==null) return null;

                Key keyReference = registry.deserializeFromFile(Key.class, e.get("item"));
                if(keyReference != null){
                    return new LootTableReferenceItemPoolObject(
                        loot.getWeight(), loot.getQuality(), loot.getConditions(), loot.getFunctions(), keyReference
                    );
                }

                LootTable<ItemStack> lootTable = registry.deserializeFromFile(ItemLootTable.class, e.get("item"));
                if(lootTable == null) return null;

                return new LootTableItemPoolObject(
                    loot.getWeight(), loot.getQuality(), loot.getConditions(), loot.getFunctions(), lootTable
                );
            }
        });
        file.registerCustomHandler(new SimpleFileItemLootPoolObject<>(Crux.key("alternatives")) {
            @Override
            public @Nullable ItemLootPoolObject deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                FileRegistry registry = ctx.getRegistry();
                SimpleItemLootObject loot = registry.deserializeFromFile(SimpleItemLootObject.class, e);
                if(loot==null) return null;

                List<LootPoolObject<ItemStack>> children = registry.deserializeFromFile(
                    new TypeToken<List<ItemLootPoolObject>>(){}.getType(), e.get("children")
                );

                if(children == null || children.isEmpty()) return null;
                return new AlternativeItemPoolObject(
                    loot.getWeight(), loot.getQuality(), loot.getConditions(), loot.getFunctions(), children
                );
            }
        });
    }
}
