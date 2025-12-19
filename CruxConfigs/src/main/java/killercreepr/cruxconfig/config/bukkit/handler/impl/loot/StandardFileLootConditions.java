package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.enchantment.CruxLevelBasedValue;
import killercreepr.crux.api.entity.predicate.EntityPredicate;
import killercreepr.crux.api.item.predicate.ItemPredicate;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.loot.conditions.*;
import killercreepr.crux.core.loot.conditions.block.BlockCondition;
import killercreepr.crux.core.loot.conditions.block.BlockDirectionalInfoCondition;
import killercreepr.crux.core.loot.conditions.block.BlockDirectionalNearbyCondition;
import killercreepr.crux.core.loot.conditions.block.BlockStateCondition;
import killercreepr.crux.core.loot.conditions.debug.DevStringCondition;
import killercreepr.crux.core.loot.conditions.entity.EntityCondition;
import killercreepr.crux.core.loot.conditions.evaluation.EvaluationCondition;
import killercreepr.crux.core.loot.conditions.evaluation.SelectNumberEvaluationCondition;
import killercreepr.crux.core.loot.conditions.item.InventoryHasItemCondition;
import killercreepr.crux.core.loot.conditions.item.ItemStackCondition;
import killercreepr.crux.core.loot.conditions.location.LocationInRegionCondition;
import killercreepr.crux.core.loot.conditions.world.LocationCondition;
import killercreepr.crux.core.loot.conditions.world.WorldCondition;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FileObject;
import net.kyori.adventure.key.Key;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

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
                return new BlockCondition(target, blockPredicate, registry.deserializeFromFile(LootCondition.class, e.get("light_level")));
            }
        });
        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("block_state")) {
            @Override
            public @NotNull BlockStateCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                BlockPredicate type = registry.deserializeFromFile(BlockPredicate.class, e.get("block"));
                NumberProvider age = registry.deserializeFromFile(NumberProvider.class, e.get("age"));
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
                    target, entityPredicate, worldName,
                    registry.deserializeFromFile(Key.class, e.get("world_key")),
                    slots,
                    registry.deserializeFromFile(String.class, e.get("uuid"))
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

        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("inventory_has_item")) {
            @Override
            public @NotNull LootCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                return new InventoryHasItemCondition(
                    target,
                    registry.deserializeFromFile(LootCondition.class, e.get("item_condition")),
                    registry.deserializeFromFileOrDefault(Integer.class, e.get("amount"), 1)
                );
            }
        });

        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("world")) {
            @Override
            public @NotNull WorldCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                String name = e.getObject(String.class, "name");
                String dimension = e.getObject(String.class, "dimension");
                Key key = registry.deserializeFromFile(Key.class, e.get("key"));
                return new WorldCondition(
                    target, name, key, dimension
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

        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("if_target")) {
            @Override
            public @Nullable killercreepr.crux.core.loot.conditions.IfTargetCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                String targetType = e.getObject(String.class, "target_type");
                if(targetType==null) return null;
                LootCondition ifTrue = registry.deserializeFromFile(LootCondition.class, e.get("if"));
                return new killercreepr.crux.core.loot.conditions.IfTargetCondition(target, targetType, ifTrue);
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

        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("random_chance_with_enchanted_bonus")) {

            @Override
            public @Nullable RandomChanceEnchantedBonusCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                CruxLevelBasedValue chance = ctx.getRegistry().deserializeFromFile(CruxLevelBasedValue.class, e.get("enchanted_chance"));
                if(chance==null) return null;
                Float unenchanted = e.getObject(Float.class, "unenchanted_chance");
                if(unenchanted==null) return null;
                Key key = ctx.getRegistry().deserializeFromFile(Key.class, e.get("enchantment"));
                if(key == null) return null;
                Collection<EquipmentSlot> slots = ctx.getRegistry().deserializeFromFile(
                    new TypeToken<Collection<EquipmentSlot>>(){}.getType(),
                    e.get("slots"));
                if(slots == null) slots = Set.of(EquipmentSlot.HAND);
                return new RandomChanceEnchantedBonusCondition(
                    target, chance, unenchanted, key, slots
                );
            }
        });

        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("random_luck_chance_with_enchanted_bonus")) {

            @Override
            public @Nullable RandomChanceEnchantedBonusCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                CruxLevelBasedValue chance = ctx.getRegistry().deserializeFromFile(CruxLevelBasedValue.class, e.get("enchanted_chance"));
                if(chance==null) return null;
                Float unenchanted = e.getObject(Float.class, "unenchanted_chance");
                if(unenchanted==null) return null;
                Key key = ctx.getRegistry().deserializeFromFile(Key.class, e.get("enchantment"));
                if(key == null) return null;
                Collection<EquipmentSlot> slots = ctx.getRegistry().deserializeFromFile(
                    new TypeToken<Collection<EquipmentSlot>>(){}.getType(),
                    e.get("slots"));
                if(slots == null) slots = Set.of(EquipmentSlot.HAND);
                Float luckMultiplier = e.getObject(Float.class, "luck_multiplier");
                return new RandomLuckChanceEnchantedBonusCondition(
                    target, chance, unenchanted, key, slots, luckMultiplier
                );
            }
        });


        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("evaluation")) {

            @Override
            public @Nullable EvaluationCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                String eva = e.getObject(String.class, "check");
                /*Map<String, String> prefixes = ctx.getRegistry().deserializeFromFile(
                    new TypeToken<Map<String, String>>(){}.getType(), e.get("prefixes")
                );*/
                return new EvaluationCondition(target, eva);
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
                if(type == null) type = "any_of";
                return new killercreepr.crux.core.loot.conditions.CollectionCondition(target, condition, type,
                    e.getOrDefaultObject(Boolean.class, "use_map_keys", false));
            }
        });

        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("invert")) {

            @Override
            public @Nullable LootCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                LootCondition condition = ctx.getRegistry().deserializeFromFile(
                    LootCondition.class, e.get("term")
                );
                if(condition == null) return null;
                return new InvertCondition(target, condition);
            }
        });

        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("bool")) {

            @Override
            public @Nullable LootCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                return new BoolCondition(target);
            }
        });

        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("string")) {
            @Override
            public @Nullable LootCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                String match = e.getObject(String.class, "match");
                boolean ignoreCase = e.getOrDefaultObject(Boolean.class, "ignore_case", false);
                boolean parseVariables = e.getOrDefaultObject(Boolean.class, "parse_variables", false);
                return new StringCondition(
                    target, match, ignoreCase, parseVariables
                );
            }
        });
        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("select_string")) {
            @Override
            public @Nullable LootCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                String match = e.getObject(String.class, "match");
                String check = e.getObject(String.class, "check");
                Collection<String> targets = ctx.getRegistry().deserializeFromFile(
                    new TypeToken<Set<String>>(){}.getType(), e.get("targets")
                );
                boolean ignoreCase = e.getOrDefaultObject(Boolean.class, "ignore_case", false);
                boolean startsWith = e.getOrDefaultObject(Boolean.class, "starts_with", false);
                return new SelectStringCondition(
                    target, match, check, targets, ignoreCase, startsWith
                );
            }
        });
        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("debug/string")) {
            @Override
            public @Nullable LootCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                String match = e.getObject(String.class, "match");
                String check = e.getObject(String.class, "check");
                Collection<String> targets = ctx.getRegistry().deserializeFromFile(
                    new TypeToken<Set<String>>(){}.getType(), e.get("targets")
                );
                boolean ignoreCase = e.getOrDefaultObject(Boolean.class, "ignore_case", false);
                return new DevStringCondition(
                    target, match, check, targets, ignoreCase
                );
            }
        });

        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("int")) {
            @Override
            public @Nullable LootCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                String operation = e.getObject(String.class, "operation");
                int value = e.getObject(Integer.class, "value");
                return new IntegerCondition(
                    target, value, operation
                );
            }
        });

        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("double")) {
            @Override
            public @Nullable LootCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                String operation = e.getObject(String.class, "operation");
                double value = e.getObject(Double.class, "value");
                return new DoubleCondition(
                    target, value, operation
                );
            }
        });

        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("long")) {
            @Override
            public @Nullable LootCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                String operation = e.getObject(String.class, "operation");
                long value = e.getObject(Long.class, "value");
                return new LongCondition(
                    target, value, operation
                );
            }
        });
        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("block_directional_nearby")) {
            @Override
            public @NotNull LootCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                LootCondition type = registry.deserializeFromFile(LootCondition.class, e.get("block"));
                Collection<BlockFace> faces = registry.deserializeFromFile(
                    new TypeToken<Collection<BlockFace>>(){}.getType(), e.get("faces"));
                if(faces == null) faces = Set.of(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);
                return new BlockDirectionalNearbyCondition(target, type, faces);
            }
        });
        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("block_directional_info")) {
            @Override
            public @NotNull LootCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                BlockFace face = registry.deserializeFromFile(BlockFace.class, e.get("default_direction"));
                return new BlockDirectionalInfoCondition(target, face,
                    e.getOrDefaultObject(Boolean.class, "opposite", false));
            }
        });
        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("select_number_evaluation")) {
            @Override
            public @Nullable LootCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                if(!(e.get("value") instanceof FileGeneric g)) return null;
                String check = e.getObject(String.class, "check");
                Collection<String> targets = ctx.getRegistry().deserializeFromFile(
                    new TypeToken<Set<String>>(){}.getType(), e.get("targets")
                );
                return new SelectNumberEvaluationCondition(target,
                    g.isNumber() ? g.getAsNumber() : g.getAsString(),
                    check, targets, e.getOrDefaultObject(String.class, "operation", "=")
                );
            }
        });
        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("location_in_region")) {
            @Override
            public @Nullable LootCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                var r = ctx.getRegistry();
                return new LocationInRegionCondition(target,
                    r.deserializeFromFile(Vector.class, e.get("pos1")),
                    r.deserializeFromFile(Vector.class, e.get("pos2")),
                    r.deserializeFromFileOrDefault(Boolean.class, e.get("use_block_pos"), false)
                );
            }
        });
        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("interact_action")) {
            @Override
            public @Nullable LootCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                var r = ctx.getRegistry();
                return new InteractActionCondition(target,
                    r.deserializeFromFile(String.class, e.get("action"))
                );
            }
        });
    }
}
