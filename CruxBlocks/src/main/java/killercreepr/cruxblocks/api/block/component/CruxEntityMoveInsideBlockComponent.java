package killercreepr.cruxblocks.api.block.component;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public interface CruxEntityMoveInsideBlockComponent extends CruxBlockComponent {
    void onEntityMoveInside(@NotNull Entity e);
}
