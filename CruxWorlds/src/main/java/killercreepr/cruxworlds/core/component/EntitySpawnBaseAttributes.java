package killercreepr.cruxworlds.core.component;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.item.dynamic.component.attribute.DynamicAttributeModifier;
import killercreepr.crux.core.util.CruxAttributeUtil;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxworlds.api.component.EntitySpawnComponent;
import killercreepr.cruxworlds.api.world.entity.SpawnContext;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;

public class EntitySpawnBaseAttributes implements EntitySpawnComponent {
    public final Map<Object, Object> attributes;

    public EntitySpawnBaseAttributes(Map<Object, Object> attributes) {
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
            double value = CruxMath.evaluate(textCtx.deserializeString(modObject + ""));
            CruxAttributeUtil.editInstance(living, attribute, instance -> instance.setBaseValue(value));
        });
    }
}
