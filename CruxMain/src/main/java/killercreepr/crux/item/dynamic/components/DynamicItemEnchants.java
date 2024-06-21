package killercreepr.crux.item.dynamic.components;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.item.dynamic.DynamicItemComponent;
import killercreepr.crux.util.CruxItem;
import killercreepr.crux.util.CruxMath;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class DynamicItemEnchants implements DynamicItemComponent {
    protected final @NotNull Map<Object, Object> enchants;
    public DynamicItemEnchants(@NotNull Map<Object, Object> enchants) {
        this.enchants = enchants;
    }

    @Override
    public @NotNull String name() {
        return "enchants";
    }

    public @NotNull Map<Object, Object> getEnchants() {
        return enchants;
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        enchants.forEach((keyObject, amountObject) ->{
            NamespacedKey key = NamespacedKey.fromString(context.deserializeString(keyObject.toString()));
            if(key==null) return;
            Enchantment enchantment = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(key);
            if(enchantment==null) return;
            int level = (int) CruxMath.evaluate(context.deserializeString(keyObject.toString()));
            item.enchant(enchantment, level);
        });
    }
}
