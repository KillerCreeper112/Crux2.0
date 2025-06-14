package killercreepr.cruxblocks.api.block.component;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public interface CruxInteractablePhysicalBlockComponent extends CruxBlockComponent {
    void onEntityPhysicalInteract(@NotNull Entity e, @NotNull PlayerInteractEvent event);
    void onEntityPhysicalInteract(@NotNull Entity e, @NotNull EntityInteractEvent event);
}
