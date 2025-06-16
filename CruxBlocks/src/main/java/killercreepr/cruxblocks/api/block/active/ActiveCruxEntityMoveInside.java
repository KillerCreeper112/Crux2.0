package killercreepr.cruxblocks.api.block.active;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface ActiveCruxEntityMoveInside {
    void onEntityMoveInside(@NotNull Entity e);
}
