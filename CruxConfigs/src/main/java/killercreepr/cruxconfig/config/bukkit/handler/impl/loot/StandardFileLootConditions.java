package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.entity.predicate.EntityPredicate;
import killercreepr.crux.api.item.predicate.ItemPredicate;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.loot.conditions.EntityOrItemCondition;
import killercreepr.crux.core.loot.conditions.block.BlockCondition;
import killercreepr.crux.core.loot.conditions.block.BlockStateCondition;
import killercreepr.crux.core.loot.conditions.entity.EntityCondition;
import killercreepr.crux.core.loot.conditions.evaluation.EvaluationCondition;
import killercreepr.crux.core.loot.conditions.item.ItemStackCondition;
import killercreepr.crux.core.loot.conditions.world.LocationCondition;
import killercreepr.crux.core.loot.conditions.world.WorldCondition;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileObject;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;

public class StandardFileLootConditions {
    public static void register(@NotNull FileLootCondition file){
        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("all_of")) {
            @Override
            public @Nullable LootCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                Collection<LootCondition> conditions = ctx.getRegistry().deserializeFromFile(
                    new TypeToken<Collection<LootCondition>>(){}.getType(),
                    e.get("terms")
                );
                if(conditions==null||conditions.isEmpty()) return null;
                return new killercreepr.crux.core.loot.conditions.AllOfCondition(conditions);
            }
        });
        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("any_of")) {

            @Override
            public @Nullable LootCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                Collection<LootCondition> conditions = ctx.getRegistry().deserializeFromFile(
                    new TypeToken<Collection<LootCondition>>(){}.getType(),
                    e.get("terms")
                );
                if(conditions==null||conditions.isEmpty()) return null;
                return new killercreepr.crux.core.loot.conditions.AnyOfCondition(conditions);
            }
        });
        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("block")) {
            @Override
            public @NotNull BlockCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                BlockPredicate blockPredicate = registry.deserializeFromFile(BlockPredicate.class, e.get("block_predicate"));
                return new BlockCondition(target, blockPredicate);
            }
        });
        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("block_state")) {
            @Override
            public @NotNull BlockStateCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                Key type = registry.deserializeFromFile(Key.class, e.get("block"));
                Integer age = registry.deserializeFromFile(Integer.class, e.get("age"));
                return new BlockStateCondition(target, type, age);
            }
        });
        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("entity")) {

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

        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("item")) {
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

        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("world")) {

            @Override
            public @NotNull WorldCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                String name = e.getObject(String.class, "name");
                String dimension = e.getObject(String.class, "dimension");
                return new WorldCondition(
                    target, name, dimension
                );
            }
        });

        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("location")) {
            @Override
            public @NotNull LocationCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                String worldName = e.getObject(String.class, "world_name");
                String worldDimension = e.getObject(String.class, "world_dimension");
                Key biome = registry.deserializeFromFile(Key.class, e.get("biome"));
                return new LocationCondition(
                    target, worldName, worldDimension, biome
                );
            }
        });

        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("entity_or_item")) {
            @Override
            public @Nullable killercreepr.crux.core.loot.conditions.EntityOrItemCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                LootCondition itemCondition = registry.deserializeFromFile(LootCondition.class, e.get("item_condition"));
                if(itemCondition==null) return null;
                Collection<EquipmentSlot> ifEntitySlots = registry.deserializeFromFile(
                    new TypeToken<Collection<EquipmentSlot>>(){}.getType(), e.get("if_entity_slots")
                );
                return new EntityOrItemCondition(target, itemCondition, ifEntitySlots);
            }
        });

        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("target_check")) {
            @Override
            public @Nullable killercreepr.crux.core.loot.conditions.TargetCheckCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                String targetType = e.getObject(String.class, "target_type");
                if(targetType==null) return null;
                LootCondition ifTrue = registry.deserializeFromFile(LootCondition.class, e.get("if"));
                LootCondition ifFalse = registry.deserializeFromFile(LootCondition.class, e.get("else"));
                return new killercreepr.crux.core.loot.conditions.TargetCheckCondition(target, targetType, ifTrue, ifFalse);
            }
        });

        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("random_chance")) {
            @Override
            public @Nullable killercreepr.crux.core.loot.conditions.RandomChanceCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                Float chance = e.getObject(Float.class, "chance");
                if(chance==null) return null;
                return new killercreepr.crux.core.loot.conditions.RandomChanceCondition(chance);
            }
        });

        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("random_luck_chance")) {

            @Override
            public @Nullable killercreepr.crux.core.loot.conditions.RandomChanceCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                Float chance = e.getObject(Float.class, "chance");
                if(chance==null) return null;
                Float luckMultiplier = e.getObject(Float.class, "luck_multiplier");
                if(luckMultiplier==null) return null;
                return new killercreepr.crux.core.loot.conditions.RandomLuckChanceCondition(chance, luckMultiplier);
            }
        });

        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("evaluation")) {

            @Override
            public @Nullable EvaluationCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                String eva = e.getObject(String.class, "check");
                Map<String, String> prefixes = ctx.getRegistry().deserializeFromFile(
                    new TypeToken<Map<String, String>>(){}.getType(), e.get("prefixes")
                );
                return new EvaluationCondition(target, eva, prefixes);
            }
        });

        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("list")) {

            @Override
            public @Nullable killercreepr.crux.core.loot.conditions.CollectionCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                LootCondition condition = ctx.getRegistry().deserializeFromFile(
                    LootCondition.class, e.get("term")
                );
                if(condition == null) return null;
                String type = e.getObject(String.class, "type");
                if(type == null) type = "all_of";
                return new killercreepr.crux.core.loot.conditions.CollectionCondition(target, condition, type);
            }
        });
    }
}
