package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.component.parser.DataComponentDecoder;
import killercreepr.crux.api.component.parser.DataComponentEncoder;
import killercreepr.crux.api.enchantment.DropFormula;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.loot.item.functions.*;
import killercreepr.crux.core.util.CruxObjects;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileObject;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class StandardFileLootFunctions {
    public static void register(@NotNull FileItemLootFunction file){
        file.registerCustomHandler(new SimpleFileItemLootFunction<>(Crux.key("set_enchants")) {

            @Override
            public @Nullable ItemEnchantFunction deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                NumberProvider rolls = registry.deserializeFromFile(NumberProvider.class, e.get("rolls"));
                if(rolls==null) rolls = NumberProvider.constant(1);

                if(!(e.get("enchants") instanceof FileArray oo)) return null;
                Collection<ItemEnchantFunction.Enchant> enchants = new HashSet<>();
                oo.forEach(ele ->{
                    if(!(ele instanceof FileObject f)){
                        Key enchantKey = registry.deserializeFromFile(Key.class, ele);
                        if(CruxObjects.checkNull(enchantKey)) return;
                        enchants.add(
                            new ItemEnchantFunction.Enchant(1, 0f, enchantKey, null)
                        );
                        return;
                    }
                    int weight = f.getObject(Integer.class, "weight", 0);
                    float quality = f.getObject(Float.class, "quality", 0f);
                    Key enchantKey = registry.deserializeFromFile(Key.class, f.get("enchant"));
                    NumberProvider level = registry.deserializeFromFile(NumberProvider.class, f.get("level"));
                    if(CruxObjects.checkNull(enchantKey)) return;
                    enchants.add(
                        new ItemEnchantFunction.Enchant(weight, quality, enchantKey, level)
                    );
                });
                if(enchants.isEmpty()) return null;

                Collection<LootCondition> conditions = registry.deserializeFromFile(
                    new TypeToken<Collection<LootCondition>>(){}.getType(), e.get("conditions")
                );
                return new ItemEnchantFunction(
                    conditions, rolls, enchants
                );
            }
        });
        file.registerCustomHandler(new SimpleFileItemLootFunction<>(Crux.key("set_count")) {
            @Override
            public @Nullable ItemAmountFunction deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                NumberProvider amount = registry.deserializeFromFile(NumberProvider.class, e.get("count"));
                if(amount==null) return null;
                Collection<LootCondition> conditions = registry.deserializeFromFile(
                    new TypeToken<Collection<LootCondition>>(){}.getType(), e.get("conditions")
                );//todo change this cause no I dont want to do this every time
                return new ItemAmountFunction(conditions, amount, e.getObject(Boolean.class, "add", false));
            }
        });
        file.registerCustomHandler(new SimpleFileItemLootFunction<>(Crux.key("set_damage")) {

            @Override
            public @Nullable ItemDamageFunction deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                NumberProvider amount = registry.deserializeFromFile(NumberProvider.class, e.get("damage"));
                if(amount==null) return null;
                Collection<LootCondition> conditions = registry.deserializeFromFile(
                    new TypeToken<Collection<LootCondition>>(){}.getType(), e.get("conditions")
                );
                return new ItemDamageFunction(conditions, amount, e.getObject(Boolean.class, "add", false));
            }
        });
        file.registerCustomHandler(new SimpleFileItemLootFunction<>(Crux.key("enchant_randomly")) {
            @Override
            public @Nullable ItemEnchantRandomlyFunction deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                NumberProvider rolls = registry.deserializeFromFile(NumberProvider.class, e.get("rolls"));
                if(rolls==null) rolls = NumberProvider.constant(1);
                Boolean distinct = registry.deserializeFromFile(Boolean.class, e.get("distinct"));
                if(distinct == null) distinct = false;
                Collection<LootCondition> conditions = registry.deserializeFromFile(
                    new TypeToken<Collection<LootCondition>>(){}.getType(), e.get("conditions")
                );
                return new ItemEnchantRandomlyFunction(conditions, rolls, distinct);
            }
        });
        file.registerCustomHandler(new SimpleFileItemLootFunction<>(Crux.key("enchanted_count")) {

            @Override
            public @Nullable EnchantedCountFunction deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                NumberProvider amount = registry.deserializeFromFile(NumberProvider.class, e.get("count"));
                if(amount==null) return null;
                Collection<LootCondition> conditions = registry.deserializeFromFile(
                    new TypeToken<Collection<LootCondition>>(){}.getType(), e.get("conditions")
                );
                Collection<EquipmentSlot> slots = registry.deserializeFromFile(
                    new TypeToken<Collection<EquipmentSlot>>(){}.getType(), e.get("slots")
                );
                Key enchant = registry.deserializeFromFile(Key.class, e.get("enchantment"));
                if(slots == null) slots = Set.of(EquipmentSlot.HAND);
                return new EnchantedCountFunction(
                    conditions,
                    target,
                    amount,
                    e.getObject(Boolean.class, "add", true),
                    enchant,
                    slots,
                    registry.deserializeFromFile(NumberProvider.class, e.get("max"))
                );
            }
        });
        file.registerCustomHandler(new SimpleFileItemLootFunction<>(Crux.key("enchant_bonus_count")) {

            @Override
            public @Nullable EnchantBonusCountFunction deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                Collection<LootCondition> conditions = registry.deserializeFromFile(
                    new TypeToken<Collection<LootCondition>>(){}.getType(), e.get("conditions")
                );
                Collection<EquipmentSlot> slots = registry.deserializeFromFile(
                    new TypeToken<Collection<EquipmentSlot>>(){}.getType(), e.get("slots")
                );
                Key enchant = registry.deserializeFromFile(Key.class, e.get("enchantment"));
                if(slots == null) slots = Set.of(EquipmentSlot.HAND);
                return new EnchantBonusCountFunction(
                    conditions,
                    target,
                    registry.deserializeFromFile(DropFormula.class, e.get("formula")),
                    enchant,
                    slots
                );
            }
        });
        file.registerCustomHandler(new SimpleFileItemLootFunction<>(Crux.key("set_base_potion")) {

            @Override
            public @Nullable SetBasePotionFunction deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                Collection<LootCondition> conditions = registry.deserializeFromFile(
                    new TypeToken<Collection<LootCondition>>(){}.getType(), e.get("conditions")
                );
                return new SetBasePotionFunction(
                    conditions,
                    registry.deserializeFromFile(PotionType.class, e.get("potion"))
                );
            }
        });
        file.registerCustomHandler(new SimpleFileItemLootFunction<>(Crux.key("set_crux_components")) {

            @Override
            public @Nullable SetCruxComponentFunction deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                Collection<LootCondition> conditions = registry.deserializeFromFile(
                    new TypeToken<Collection<LootCondition>>(){}.getType(), e.get("conditions")
                );
                return new SetCruxComponentFunction(
                    conditions,
                    DataComponentDecoder.componentDecoder().parseComponents(e.get("components").getAsString())
                );
            }
        });
    }
}
