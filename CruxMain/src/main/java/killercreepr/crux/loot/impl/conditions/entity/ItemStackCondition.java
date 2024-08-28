package killercreepr.crux.loot.impl.conditions.entity;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import killercreepr.crux.Crux;
import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.impl.conditions.BaseCondition;
import killercreepr.crux.tags.container.TagContainer;
import killercreepr.crux.tags.resolver.Tag;
import killercreepr.crux.util.CruxMath;
import killercreepr.crux.util.CruxString;
import net.kyori.adventure.key.Key;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ItemStackCondition extends BaseCondition {
    protected final @Nullable Key itemType;
    protected final @Nullable String amount;
    protected final @Nullable Map<Key, String> enchants;

    public ItemStackCondition(@NotNull String target, @Nullable Key itemType, @Nullable String amount, @Nullable Map<Key, String> enchants) {
        super(target);
        this.itemType = itemType;
        this.amount = amount;
        this.enchants = enchants;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        ItemStack item = ctx.info().get(target, ItemStack.class);
        if(item==null) return false;
        if(itemType != null){
            if(!Crux.handlers().item().getType(item).equals(itemType)) return false;
        }
        if(amount != null){
            if(!CruxString.parseBoolean(CruxMath.evaluateEvalEx(
                Crux.FORMAT.deserializeString(amount, TagContainer.string(Tag.parsed("amount", item.getAmount()+"")).hook(item))
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
                    Crux.FORMAT.deserializeString(equation, TagContainer.string(Tag.parsed("level", level+"")).hook(item))
                ))) return false;
            }
        }
        return true;
    }
}
