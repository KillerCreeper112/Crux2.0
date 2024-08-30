package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.loot.conditions.LootCondition;
import killercreepr.crux.loot.impl.conditions.AllOfCondition;
import killercreepr.crux.loot.impl.conditions.AnyOfCondition;
import killercreepr.crux.loot.impl.conditions.block.BlockCondition;
import killercreepr.crux.loot.impl.conditions.entity.EntityCondition;
import killercreepr.crux.loot.impl.conditions.entity.ItemStackCondition;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileObject;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public class StandardFileLootConditions {
    public static void register(@NotNull FileLootCondition file){
        file.registerCustomHandler(new CustomFileLootCondition<>() {
            @Override
            public @NotNull String getType() {
                return "all_of";
            }

            @Override
            public @Nullable LootCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                Collection<LootCondition> conditions = ctx.getRegistry().deserializeFromFile(
                    new TypeToken<Collection<LootCondition>>(){}.getType(),
                    e.get("terms")
                );
                if(conditions==null||conditions.isEmpty()) return null;
                return new AllOfCondition(conditions);
            }
        });
        file.registerCustomHandler(new CustomFileLootCondition<>() {
            @Override
            public @NotNull String getType() {
                return "any_of";
            }

            @Override
            public @Nullable LootCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                Collection<LootCondition> conditions = ctx.getRegistry().deserializeFromFile(
                    new TypeToken<Collection<LootCondition>>(){}.getType(),
                    e.get("terms")
                );
                if(conditions==null||conditions.isEmpty()) return null;
                return new AnyOfCondition(conditions);
            }
        });
        file.registerCustomHandler(new CustomFileLootCondition<>() {
            @Override
            public @NotNull String getType() {
                return "block";
            }

            @Override
            public @NotNull BlockCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                Key blockType = registry.deserializeFromFile(Key.class, e.get("block_type"));
                return new BlockCondition(target, blockType);
            }
        });
        file.registerCustomHandler(new CustomFileLootCondition<>() {
            @Override
            public @NotNull String getType() {
                return "entity";
            }

            @Override
            public @NotNull EntityCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                Key entityType = registry.deserializeFromFile(Key.class, e.get("entity_type"));
                String worldName = registry.deserializeFromFile(String.class, e.get("world"));
                Map<EquipmentSlot, LootCondition> slots = registry.deserializeFromFile(
                    new TypeToken<Map<EquipmentSlot, LootCondition>>(){}.getType(), e.get("slots")
                );
                return new EntityCondition(
                    target, entityType, worldName, slots
                );
            }
        });

        file.registerCustomHandler(new CustomFileLootCondition<>() {
            @Override
            public @NotNull String getType() {
                return "item";
            }

            @Override
            public @NotNull ItemStackCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                Key itemType = registry.deserializeFromFile(Key.class, e.get("item_type"));
                String amount = registry.deserializeFromFile(String.class, e.get("amount"));
                Map<Key, String> enchants = registry.deserializeFromFile(
                    new TypeToken<Map<Key, String>>(){}.getType(), e.get("enchants")
                );
                return new ItemStackCondition(
                    target, itemType, amount, enchants
                );
            }
        });
    }
}
