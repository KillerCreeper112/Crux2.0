package killercreepr.cruxworlds.core.component;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.item.dynamic.component.attribute.DynamicAttributeModifier;
import killercreepr.cruxworlds.api.component.EntitySpawnComponent;
import killercreepr.cruxworlds.api.world.entity.SpawnContext;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;

public class EntitySpawnAttributes implements EntitySpawnComponent {
    public final Map<Object, Collection<DynamicAttributeModifier>> attributes;

    public EntitySpawnAttributes(Map<Object, Collection<DynamicAttributeModifier>> attributes) {
        this.attributes = attributes;
    }

    @Override
    public void onCreateEntity(@NotNull SpawnContext ctx, Entity e, TextParserContext textCtx) {
        if(!(e instanceof LivingEntity living)) return;

        attributes.forEach((keyObject, modObject) ->{
            NamespacedKey key = NamespacedKey.fromString(textCtx.deserializeString(keyObject.toString()));
            if(key==null) return;

            Attribute attribute = Registry.ATTRIBUTE.get(key);
            if(attribute == null) return;
            AttributeInstance instance = living.getAttribute(attribute);
            if(instance == null) return;

            modObject.forEach(mod ->{
                AttributeModifier m = mod.buildModifier(attribute, textCtx);
                instance.addModifier(m);
            });
        });
    }
}
