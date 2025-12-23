package killercreepr.cruxworlds.api.component;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.cruxworlds.api.world.entity.SpawnContext;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface EntitySpawnComponent {
    void onCreateEntity(@NotNull SpawnContext ctx, Entity e, TextParserContext textCtx);
}
