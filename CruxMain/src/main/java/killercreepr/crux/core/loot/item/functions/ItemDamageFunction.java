package killercreepr.crux.core.loot.item.functions;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.loot.item.ItemLootFunction;
import killercreepr.crux.api.text.context.InputContext;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.loot.functions.SimpleLootFunction;
import killercreepr.crux.core.util.CruxMath;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Random;

public class ItemDamageFunction extends SimpleLootFunction<ItemStack> implements ItemLootFunction {
    private final @NotNull NumberProvider amount;
    private final boolean add;
    public ItemDamageFunction(@Nullable Collection<LootCondition> conditions, @NotNull NumberProvider amount, boolean add) {
        super(conditions);
        this.amount = amount;
        this.add = add;
    }


    @Override
    public ItemStack accept(@Nullable ItemStack i, @NotNull LootContext context) {
        if(i==null) return i;
        i.editMeta(Damageable.class, meta ->{
            int maxDamage = CruxItem.getMaxDurability(i);
            if(maxDamage < 1) return;
            Random source = context.getRandom();
            InputContext inputCtx = InputContext.simple(TagContainer.string().hook(i));
            int dmg = this.amount.sample(source, inputCtx).intValue();
            if(add) dmg = meta.getDamage() + dmg;
            meta.setDamage(CruxMath.clamp(dmg, 0, maxDamage));
        });
        return i;
    }
}
