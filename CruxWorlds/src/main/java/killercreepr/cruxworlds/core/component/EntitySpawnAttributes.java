package killercreepr.cruxworlds.core.component;

import killercreepr.crux.core.item.dynamic.component.attribute.DynamicAttributeModifier;

import java.util.Collection;
import java.util.Map;

public class EntitySpawnAttributes {
    public final Map<Object, Collection<DynamicAttributeModifier>> attributes;

    public EntitySpawnAttributes(Map<Object, Collection<DynamicAttributeModifier>> attributes) {
        this.attributes = attributes;
    }
}
