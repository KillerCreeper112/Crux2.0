package killercreepr.crux.core.item.dynamic.component;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.item.dynamic.DynamicItemComponent;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.item.dynamic.component.attribute.DynamicAttributeModifier;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DynamicItemAttributes extends SimpleDynamicItemComponent {
    protected final @NotNull Map<Object, Collection<DynamicAttributeModifier>> attributes;

    public DynamicItemAttributes(@NotNull Map<Object, Collection<DynamicAttributeModifier>> attributes) {
        this.attributes = attributes;
    }

    public @NotNull Map<Object, Collection<DynamicAttributeModifier>> getAttributes() {
        return attributes;
    }

    @Override
    public @NotNull String name() {
        return "attributes";
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        attributes.forEach((keyObject, modObject) ->{
            NamespacedKey key = NamespacedKey.fromString(context.deserializeString(keyObject.toString()));
            if(key==null) return;

            Attribute attribute = Registry.ATTRIBUTE.get(key);
            if(attribute == null) return;

            modObject.forEach(mod ->{
                AttributeModifier m = mod.buildModifier(attribute, context);
                item.addAttribute(attribute, m);
            });
        });
    }

    @Override
    public @NotNull DynamicItemComponent merge(@NotNull DynamicItemComponent with) {
        if(!(with instanceof DynamicItemAttributes w)) return this;
        Map<Object, Collection<DynamicAttributeModifier>> map = new HashMap<>(attributes);
        map.putAll(w.getAttributes());
        return new DynamicItemAttributes(map);
    }
}
