package killercreepr.cruxblocks.api.block.active;

import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public interface ActiveCruxEntityPhysicalInteract {
    void onEntityPhysicalInteract(@NotNull Entity e, @NotNull PlayerInteractEvent event);
}
