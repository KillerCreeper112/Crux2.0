package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.crux.api.loot.LootPoolObject;
import killercreepr.crux.api.loot.item.ItemLootPoolObject;
import killercreepr.crux.core.loot.item.SimpleItemLootObject;
import killercreepr.crux.core.loot.item.pool.AlternativeItemPoolObject;
import killercreepr.crux.core.loot.item.pool.ListItemPoolObject;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class StandardFileLootPoolObjects {
    public static void register(@NotNull FileItemLootPoolObject file){
        file.registerCustomHandler(new CustomFilePoolObject<>() {
            @Override
            public @NotNull String getType() {
                return "item";
            }

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
                    if(items == null) return null;
                }

                if(items.isEmpty()) return null;
                return new ListItemPoolObject(
                    loot.getWeight(), loot.getQuality(), loot.getConditions(), loot.getFunctions(), items
                );
            }
        });
        file.registerCustomHandler(new CustomFilePoolObject<>() {
            @Override
            public @NotNull String getType() {
                return "alternatives";
            }

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
