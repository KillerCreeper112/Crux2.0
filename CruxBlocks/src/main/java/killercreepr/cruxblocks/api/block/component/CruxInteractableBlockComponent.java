package killercreepr.cruxblocks.api.block.component;

import killercreepr.cruxblocks.api.block.active.ActiveCruxBlock;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CruxInteractableBlockComponent extends CruxBlockComponent {
    @Nullable Event.Result interact(@NotNull ActiveCruxBlock block, @NotNull PlayerInteractEvent event);
}
