package killercreepr.crux.core.item.dynamic.component;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import killercreepr.crux.api.item.dynamic.DynamicItemComponent;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.item.SimpleCruxItem;
import killercreepr.crux.core.util.CruxMath;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
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
    public void apply(@NotNull SimpleCruxItem item, @NotNull TextParserContext context) {
        enchants.forEach((keyObject, amountObject) ->{
            NamespacedKey key = NamespacedKey.fromString(context.deserializeString(keyObject.toString()));
            if(key==null) return;
            Enchantment enchantment = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(key);
            if(enchantment==null) return;
            int level = (int) CruxMath.evaluate(context.deserializeString(amountObject.toString()));
            item.enchant(enchantment, level);
        });
    }

    @Override
    public @NotNull DynamicItemComponent merge(@NotNull DynamicItemComponent with) {
        if(!(with instanceof DynamicItemEnchants w)) return this;
        Map<Object, Object> map = new HashMap<>(enchants);
        map.putAll(w.getEnchants());//todo maybe some way to add to the enchant's level instead of replacing the existing one. IDK
        return new DynamicItemEnchants(map);
    }
}
