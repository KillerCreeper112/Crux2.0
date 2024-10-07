package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.Crux;
import killercreepr.crux.block.predicate.BlockPredicate;
import killercreepr.crux.entity.predicate.EntityPredicate;
import killercreepr.crux.item.predicate.ItemPredicate;
import killercreepr.crux.loot.conditions.LootCondition;
import killercreepr.crux.loot.impl.conditions.*;
import killercreepr.crux.loot.impl.conditions.block.BlockCondition;
import killercreepr.crux.loot.impl.conditions.entity.EntityCondition;
import killercreepr.crux.loot.impl.conditions.evaluation.EvaluationCondition;
import killercreepr.crux.loot.impl.conditions.item.ItemStackCondition;
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
                BlockPredicate blockPredicate = registry.deserializeFromFile(BlockPredicate.class, e.get("block_predicate"));
                return new BlockCondition(target, blockPredicate);
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
                EntityPredicate entityPredicate = registry.deserializeFromFile(EntityPredicate.class, e.get("entity_predicate"));
                String worldName = registry.deserializeFromFile(String.class, e.get("world"));
                Map<EquipmentSlot, LootCondition> slots = registry.deserializeFromFile(
                    new TypeToken<Map<EquipmentSlot, LootCondition>>(){}.getType(), e.get("slots")
                );
                return new EntityCondition(
                    target, entityPredicate, worldName, slots
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
                ItemPredicate itemType = registry.deserializeFromFile(ItemPredicate.class, e.get("item_predicate"));
                String amount = registry.deserializeFromFile(String.class, e.get("amount"));
                Map<Key, String> enchants = registry.deserializeFromFile(
                    new TypeToken<Map<Key, String>>(){}.getType(), e.get("enchants")
                );
                return new ItemStackCondition(
                    target, itemType, amount, enchants
                );
            }
        });

        file.registerCustomHandler(new CustomFileLootCondition<>() {
            @Override
            public @NotNull String getType() {
                return "entity_or_item";
            }

            @Override
            public @Nullable EntityOrItemCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                LootCondition itemCondition = registry.deserializeFromFile(LootCondition.class, e.get("item_condition"));
                if(itemCondition==null) return null;
                Collection<EquipmentSlot> ifEntitySlots = registry.deserializeFromFile(
                    new TypeToken<Collection<EquipmentSlot>>(){}.getType(), e.get("if_entity_slots")
                );
                return new EntityOrItemCondition(target, itemCondition, ifEntitySlots);
            }
        });

        file.registerCustomHandler(new CustomFileLootCondition<>() {
            @Override
            public @NotNull String getType() {
                return "target_check";
            }

            @Override
            public @Nullable TargetCheckCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                String targetType = e.getObject(String.class, "target_type");
                if(targetType==null) return null;
                LootCondition ifTrue = registry.deserializeFromFile(LootCondition.class, e.get("if"));
                LootCondition ifFalse = registry.deserializeFromFile(LootCondition.class, e.get("else"));
                return new TargetCheckCondition(target, targetType, ifTrue, ifFalse);
            }
        });

        file.registerCustomHandler(new CustomFileLootCondition<>() {
            @Override
            public @NotNull String getType() {
                return "random_chance";
            }

            @Override
            public @Nullable RandomChanceCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                Float chance = e.getObject(Float.class, "chance");
                if(chance==null) return null;
                return new RandomChanceCondition(chance);
            }
        });

        file.registerCustomHandler(new CustomFileLootCondition<>() {
            @Override
            public @NotNull String getType() {
                return "random_luck_chance";
            }

            @Override
            public @Nullable RandomChanceCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                Float chance = e.getObject(Float.class, "chance");
                if(chance==null) return null;
                Float luckMultiplier = e.getObject(Float.class, "luck_multiplier");
                if(luckMultiplier==null) return null;
                return new RandomLuckChanceCondition(chance, luckMultiplier);
            }
        });

        file.registerCustomHandler(new CustomFileLootCondition<>() {
            @Override
            public @NotNull String getType() {
                return "evaluation";
            }

            @Override
            public @Nullable EvaluationCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                String eva = e.getObject(String.class, "check");
                Map<String, String> prefixes = ctx.getRegistry().deserializeFromFile(
                    new TypeToken<Map<String, String>>(){}.getType(), e.get("prefixes")
                );
                return new EvaluationCondition(target, eva, prefixes);
            }
        });
    }
}
