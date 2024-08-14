package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.loot.item.SimpleItemLootObject;
import killercreepr.crux.loot.item.api.ItemLootPoolObject;
import killercreepr.crux.loot.item.pool.ListItemPoolObject;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FileObject;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;

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
                SimpleItemLootObject loot = registry.deserialize(SimpleItemLootObject.class, e);
                if(loot==null) return null;

                Collection<Key> itemKeys;
                if(e.get("item") instanceof FileGeneric){
                    Key k = registry.deserialize(Key.class, e.get("item"));
                    if(k==null) return null;
                    itemKeys = new HashSet<>();
                    itemKeys.add(k);
                }else itemKeys = registry.deserialize(
                    new TypeToken<Collection<Key>>(){}.getType(), e.get("item")
                );

                if(itemKeys == null || itemKeys.isEmpty()) return null;
                return new ListItemPoolObject(
                    loot.getWeight(), loot.getQuality(), loot.getConditions(), loot.getFunctions(), itemKeys
                );
            }
        });
    }
}
