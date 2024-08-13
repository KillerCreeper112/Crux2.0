package killercreepr.crux.loot.item.functions;

import killercreepr.crux.loot.SimpleConditionedObject;
import killercreepr.crux.loot.SimpleLootTable;
import killercreepr.crux.loot.SimpleWeighted;
import killercreepr.crux.loot.api.LootContext;
import killercreepr.crux.loot.api.conditions.LootCondition;
import killercreepr.crux.loot.functions.SimpleLootFunction;
import killercreepr.crux.valueproviders.number.NumberProvider;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ItemEnchantFunction extends SimpleLootFunction<ItemStack> {
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
        Random source = context.getRandom();
        List<Enchant> random = SimpleLootTable.randomWeighted(enchants, rolls.sample(source).intValue(), context);
        for(Enchant e : random){
            int level = e.getLevelProvider().sample(source).intValue();
            if(level < 1) continue;
            //todo CustomEnchant.set(i, e.getEnchant(), level);
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
