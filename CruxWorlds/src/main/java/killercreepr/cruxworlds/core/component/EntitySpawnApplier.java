package killercreepr.cruxworlds.core.component;

import killercreepr.crux.api.entity.CruxEntity;
import killercreepr.crux.api.entity.dynamic.DynamicEntityApplier;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.cruxworlds.api.component.EntitySpawnComponent;
import killercreepr.cruxworlds.api.world.entity.SpawnContext;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

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
