package killercreepr.crux.core.loot.conditions.item;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import killercreepr.crux.api.item.predicate.ItemPredicate;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.loot.conditions.BaseCondition;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.crux.core.util.CruxString;
import net.kyori.adventure.key.Key;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ItemStackCondition extends BaseCondition {
    protected final @Nullable ItemPredicate itemPredicate;
    protected final @Nullable String amount;
    protected final @Nullable Map<Key, String> enchants;

    public ItemStackCondition(@NotNull String target, @Nullable ItemPredicate itemPredicate, @Nullable String amount, @Nullable Map<Key, String> enchants) {
        super(target);
        this.itemPredicate = itemPredicate;
        this.amount = amount;
        this.enchants = enchants;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        ItemStack item = ctx.info().get(target, ItemStack.class);
        if(item == null && ctx.info().get(target) instanceof Item it) item = it.getItemStack();
        if(item==null) return false;
        if(itemPredicate != null){
            if(!itemPredicate.test(item)) return false;
        }
        if(amount != null){
            if(!CruxString.parseBoolean(CruxMath.evaluateEvalEx(
                Crux.format().deserializeString(amount, TagContainer.string(Tag.parsed("amount", item.getAmount()+"")).hook(item))
            ))) return false;
        }
        if(enchants != null){
            ItemMeta meta = item.getItemMeta();
            for(Map.Entry<Key, String> entry : enchants.entrySet()){
                Key enchant = entry.getKey();

                Enchantment enchantment = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(enchant);
                if(enchantment==null) continue;

                String equation = entry.getValue();
                int level = meta == null ? 0 : meta.getEnchantLevel(enchantment);

                if(!CruxString.parseBoolean(CruxMath.evaluateEvalEx(
                    Crux.format().deserializeString(equation, TagContainer.string(Tag.parsed("level", level+"")).hook(item))
                ))) return false;
            }
        }
        return true;
    }
}
