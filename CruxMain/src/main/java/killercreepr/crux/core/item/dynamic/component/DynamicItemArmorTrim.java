package killercreepr.crux.core.item.dynamic.component;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.item.dynamic.DynamicItemComponent;
import killercreepr.crux.api.text.context.TextParserContext;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DynamicItemArmorTrim implements DynamicItemComponent {
    protected final @NotNull Object trimMaterial;
    protected final @NotNull Object trimPattern;
    public DynamicItemArmorTrim(@NotNull Object trimMaterial, @NotNull Object trimPattern) {
        this.trimMaterial = trimMaterial;
        this.trimPattern = trimPattern;
    }

    public @NotNull Object getTrimMaterial() {
        return trimMaterial;
    }

    public @NotNull Object getTrimPattern() {
        return trimPattern;
    }

    @Override
    public @NotNull String name() {
        return "armor_trim";
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        item.editMeta(ArmorMeta.class, meta -> meta.setTrim(parseObject(context)));
    }

    public @Nullable ArmorTrim parseObject(@NotNull TextParserContext context){
        NamespacedKey key = NamespacedKey.fromString(context.deserializeString(trimMaterial.toString()));
        if(key==null) return null;
        TrimMaterial m = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(key);
        if(m==null) return null;
        key = NamespacedKey.fromString(context.deserializeString(trimPattern.toString()));
        if(key==null) return null;
        TrimPattern p = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_PATTERN).get(key);
        if(p==null) return null;
        return new ArmorTrim(m, p);
    }
}
