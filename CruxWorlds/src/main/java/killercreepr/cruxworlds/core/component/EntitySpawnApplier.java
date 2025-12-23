package killercreepr.cruxworlds.core.component;

import killercreepr.crux.api.entity.CruxEntity;
import killercreepr.crux.api.entity.dynamic.DynamicEntityApplier;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.item.dynamic.component.attribute.DynamicAttributeModifier;
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

public class EntitySpawnApplier implements EntitySpawnComponent {
    public final DynamicEntityApplier applier;

    public EntitySpawnApplier(DynamicEntityApplier applier) {
        this.applier = applier;
    }


    @Override
    public void onCreateEntity(@NotNull SpawnContext ctx, Entity e, TextParserContext textCtx) {
        applier.apply(CruxEntity.entity(e), textCtx);
    }
}
