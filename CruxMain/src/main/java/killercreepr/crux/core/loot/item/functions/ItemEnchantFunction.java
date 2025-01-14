package killercreepr.crux.core.loot.item.functions;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.loot.item.ItemLootFunction;
import killercreepr.crux.api.util.CruxWeightedSupplier;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.loot.SimpleWeighted;
import killercreepr.crux.core.loot.functions.SimpleLootFunction;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ItemEnchantFunction extends SimpleLootFunction<ItemStack> implements ItemLootFunction {
    private final @NotNull NumberProvider rolls;
    private final @NotNull Collection<Enchant> enchants;

    public ItemEnchantFunction(@Nullable Collection<LootCondition> conditions, @NotNull NumberProvider rolls, @NotNull Collection<Enchant> enchants) {
        super(conditions);
        this.rolls = rolls;
        this.enchants = enchants;
    }

    public ItemEnchantFunction(@NotNull NumberProvider rolls, @NotNull Collection<Enchant> enchants) {
        this(null, rolls, enchants);
    }

    @Override
    public ItemStack accept(@Nullable ItemStack i, @NotNull LootContext context) {
        if(i==null) return i;
        Random source = context.getRandom();
        List<Enchant> random = CruxWeightedSupplier.builder(enchants)
            .rolls(rolls.sample(source).intValue())
            .applyContext(context)
            .build().rollList();
        for(Enchant e : random){
            int level = e.getLevelProvider().sample(source).intValue();
            if(level < 1) continue;
            i.editMeta(meta ->{
                meta.addEnchant(RegistryAccess.registryAccess().getRegistry(
                    RegistryKey.ENCHANTMENT
                ).get(e.getEnchant()), level, true);
            });
        }
        return i;
    }

    public final static class Enchant extends SimpleWeighted {
        private final @NotNull Key enchant;
        private final @NotNull NumberProvider levelProvider;
        public Enchant(int weight, float quality, @NotNull Key enchant, @NotNull NumberProvider levelProvider) {
            super(weight, quality);
            this.enchant = enchant;
            this.levelProvider = levelProvider;
        }

        public @NotNull Key getEnchant() {
            return enchant;
        }

        public @NotNull NumberProvider getLevelProvider() {
            return levelProvider;
        }
    }
}
